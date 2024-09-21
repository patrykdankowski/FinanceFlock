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
    public Long createExpense(final ExpenseCreateDto expenseCreateDto, final String userIp) {

        final UserDomainEntity loggedUser = authenticationService.getUserFromContext();


//        final ExpenseDtoWriteModel expenseDtoValidated = expenseGeolocationService.prepareExpense(expenseDtoWriteModel, userIp);
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

        return savedExpense.getId();

    }

    @Override
    public void updateExpense(final Long id, final ExpenseUpdateDto expenseUpdateDto) {
        final UserDomainEntity loggedUser = authenticationService.getUserFromContext();
        final ExpenseDomainEntity expenseById = expenseCommandService.findExpenseById(id);


        // its used to check if expense is in same group but u are not a owner (u are admin) - different response
//        final UserDomainEntity userFromGivenIdExpense = userCommandService.findUserById(expenseById.getUserId());

        expenseValidator.validateAccessToExpense(loggedUser, expenseById);

        ExpenseUpdateVO expenseUpdateVO = new ExpenseUpdateVO(
                expenseUpdateDto.getDescription(),
                expenseUpdateDto.getAmount(),
                expenseUpdateDto.getLocation(),
                expenseUpdateDto.getExpenseDate()
        );

        expenseManagementDomain.updateExpense(expenseUpdateVO, expenseById);

        // connection between user and expense already exists so we don't have to save user separately

        expenseCommandService.saveExpense(expenseById);
    }
//    @Override
//    public void updateExpense(final Long id, final ExpenseCreateDto expenseSourceDto) {
//        final UserDomainEntity loggedUser = authenticationService.getUserFromContext();
//        final ExpenseDomainEntity expenseById = expenseCommandService.findExpenseById(id);
//
//
//        // its used to check if expense is in same group but u are not a owner (u are admin) - different response
//        final UserDomainEntity userFromGivenIdExpense = userCommandService.findUserById(expenseById.getUserId());
//
//        expenseValidator.validateAccessToExpense(loggedUser, userFromGivenIdExpense, expenseById);
//
//        ExpenseCreateVO expenseCreateVO = new ExpenseCreateVO(
//                expenseSourceDto.getDescription(),
//                new AmountVO(expenseSourceDto.getAmount()),
//                expenseSourceDto.getLocation(),
//                expenseSourceDto.getExpenseDate()
//        );
//
//        expenseManagementDomain.updateExpense(expenseCreateVO, expenseById);
//
//        // connection between user and expense already exists so we don't have to save user separately
//
//        expenseCommandService.saveExpense(expenseById);
//    }

    @Override
    public void deleteExpense(final Long id) {
        final UserDomainEntity loggedUser = authenticationService.getUserFromContext();
        final ExpenseDomainEntity expenseById = expenseCommandService.findExpenseById(id);

        expenseValidator.validateAccessToExpense(loggedUser, expenseById);

        loggedUser.removeExpense(id);
        userCommandService.saveUser(loggedUser);
        expenseCommandService.deleteExpense(id);


    }

    private void prepareExpenseIfLocationIsNull(final ExpenseCreateDto expenseCreateDto, final String userIp) {
        if (expenseCreateDto.getLocation() == null || expenseCreateDto.getLocation().isEmpty()) {
            expenseGeolocationService.setLocationForExpenseFromUserIp(expenseCreateDto, userIp);

        }
    }


}
