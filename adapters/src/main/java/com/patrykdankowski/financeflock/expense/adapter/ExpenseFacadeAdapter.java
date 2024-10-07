package com.patrykdankowski.financeflock.expense.adapter;

import com.patrykdankowski.financeflock.auth.port.AuthenticationServicePort;
import com.patrykdankowski.financeflock.expense.dto.ExpenseUpdateDto;
import com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity;
import com.patrykdankowski.financeflock.expense.dto.ExpenseCreateDto;
import com.patrykdankowski.financeflock.expense.model.vo.AmountVO;
import com.patrykdankowski.financeflock.expense.model.vo.ExpenseUpdateVO;
import com.patrykdankowski.financeflock.expense.port.ExpenseCommandServicePort;
import com.patrykdankowski.financeflock.expense.port.ExpenseFacadePort;
import com.patrykdankowski.financeflock.external_api.ExpenseGeolocationServicePort;
import com.patrykdankowski.financeflock.expense.port.ExpenseManagementDomainPort;
import com.patrykdankowski.financeflock.expense.model.vo.ExpenseCreateVO;
import com.patrykdankowski.financeflock.expense.port.ExpenseValidatorPort;
import com.patrykdankowski.financeflock.user.port.UserCommandServicePort;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
class ExpenseFacadeAdapter implements ExpenseFacadePort {


    ExpenseFacadeAdapter(
            final ExpenseManagementDomainPort expenseManagementDomain,
            final AuthenticationServicePort authenticationService,
            final ExpenseCommandServicePort expenseCommandService,
            final ExpenseGeolocationServicePort expenseGeolocationService,
            final UserCommandServicePort userCommandService,
            final ExpenseValidatorPort expenseValidator) {

        this.expenseManagementDomain = expenseManagementDomain;
        this.authenticationService = authenticationService;
        this.expenseCommandService = expenseCommandService;
        this.expenseGeolocationService = expenseGeolocationService;
        this.userCommandService = userCommandService;
        this.expenseValidator = expenseValidator;
    }


    private final ExpenseManagementDomainPort expenseManagementDomain;
    private final AuthenticationServicePort authenticationService;
    private final ExpenseCommandServicePort expenseCommandService;
    private final ExpenseGeolocationServicePort expenseGeolocationService;
    private final UserCommandServicePort userCommandService;
    private final ExpenseValidatorPort expenseValidator;


    @Override
    @Transactional
    public Long createExpense(final ExpenseCreateDto expenseCreateDto, final String userIp) {
        log.info("Starting process of create expense");

        final UserDomainEntity loggedUser = authenticationService.getFullUserFromContext();


        prepareExpenseIfLocationIsNull(expenseCreateDto, userIp);

        ExpenseCreateVO expenseCreateVO = new ExpenseCreateVO(
                expenseCreateDto.getDescription(),
                new AmountVO(expenseCreateDto.getAmount()),
                expenseCreateDto.getLocation(),
                expenseCreateDto.getExpenseDate()
        );

        final ExpenseDomainEntity expenseDomainEntity = expenseManagementDomain.createExpense(expenseCreateVO,
                loggedUser);

        final ExpenseDomainEntity savedExpense = expenseCommandService.saveExpense(expenseDomainEntity);

        loggedUser.addExpense(savedExpense.getId());
        // saving user must be after saving expense because we need expense id to connect expense with user
        userCommandService.saveUser(loggedUser);
        log.info("Successfully finished process of creating expense with ID: {}", savedExpense.getId());

        return savedExpense.getId();

    }

    @Override
    @Transactional
    public void updateExpense(final Long id, final ExpenseUpdateDto expenseUpdateDto) {
        log.info("Starting process of update expense");

        final UserDomainEntity loggedUser = authenticationService.getFullUserFromContext();
        final ExpenseDomainEntity expenseById = expenseCommandService.findExpenseById(id);

        expenseValidator.validateAccessToExpense(loggedUser, expenseById);

        ExpenseUpdateVO expenseUpdateVO = new ExpenseUpdateVO(
                expenseUpdateDto.getDescription(),
                expenseUpdateDto.getAmount(),
                expenseUpdateDto.getLocation(),
                expenseUpdateDto.getExpenseDate()
        );

        expenseManagementDomain.updateExpense(expenseUpdateVO, expenseById);

        expenseCommandService.saveExpense(expenseById);
        log.info("Successfully finished process of updating expense with ID: {}", expenseById.getId());
    }

    @Override
    @Transactional
    public void deleteExpense(final Long id) {

        log.info("Starting process of delete expense");

        final UserDomainEntity loggedUser = authenticationService.getFullUserFromContext();
        final ExpenseDomainEntity expenseById = expenseCommandService.findExpenseById(id);

        expenseValidator.validateAccessToExpense(loggedUser, expenseById);

        loggedUser.removeExpense(id);
        userCommandService.saveUser(loggedUser);
        expenseCommandService.deleteExpense(id);

        log.info("Successfully finished process of deleting expense with ID: {}", expenseById.getId());

    }

    private void prepareExpenseIfLocationIsNull(final ExpenseCreateDto expenseCreateDto, final String userIp) {
        if (expenseCreateDto.getLocation() == null || expenseCreateDto.getLocation().isEmpty()) {
            log.info("Location is null, connecting to external API");
            expenseGeolocationService.setLocationForExpenseFromUserIp(expenseCreateDto, userIp);

        }
    }


}