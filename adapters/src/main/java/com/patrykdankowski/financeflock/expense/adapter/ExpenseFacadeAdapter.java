package com.patrykdankowski.financeflock.expense.adapter;

import com.patrykdankowski.financeflock.auth.port.AuthenticationServicePort;
import com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity;
import com.patrykdankowski.financeflock.expense.dto.ExpenseDto;
import com.patrykdankowski.financeflock.expense.port.ExpenseCommandServicePort;
import com.patrykdankowski.financeflock.expense.port.ExpenseFacadePort;
import com.patrykdankowski.financeflock.external_api.ExpenseGeolocationServicePort;
import com.patrykdankowski.financeflock.expense.port.ExpenseManagementDomainPort;
import com.patrykdankowski.financeflock.expense.model.vo.ExpenseValueObject;
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
    public Long createExpense(final ExpenseDto expenseDto, final String userIp) {

        final UserDomainEntity loggedUser = authenticationService.getUserFromContext();


//        final ExpenseDtoWriteModel expenseDtoValidated = expenseGeolocationService.prepareExpense(expenseDtoWriteModel, userIp);
        prepareExpenseIfLocationIsNull(expenseDto, userIp);

        ExpenseValueObject expenseValueObject = new ExpenseValueObject(
                expenseDto.getDescription(),
                expenseDto.getAmount(),
                expenseDto.getLocation(),
                expenseDto.getExpenseDate()
        );

        final ExpenseDomainEntity expenseDomainEntity = expenseManagementDomain.createExpense(expenseValueObject,
                loggedUser);

        final ExpenseDomainEntity savedExpense = expenseCommandService.saveExpense(expenseDomainEntity);

        loggedUser.addExpense(savedExpense.getId());
        log.info(loggedUser.getExpenseListId().toString());

        // saving user must be after saving expense because we need expense id to connect expense with user
        userCommandService.saveUser(loggedUser);

        return savedExpense.getId();

    }

    @Override
    public void updateExpense(final Long id,final ExpenseDto expenseSourceDto) {
        final UserDomainEntity loggedUser = authenticationService.getUserFromContext();
        final ExpenseDomainEntity expenseById = expenseCommandService.findExpenseById(id);


        // its used to check if expense is in same group but u are not a owner (u are admin) - different response
        final UserDomainEntity userFromGivenIdExpense = userCommandService.findUserById(expenseById.getUserId());

        expenseValidator.validateAccessToExpense(loggedUser, userFromGivenIdExpense, expenseById);

        ExpenseValueObject expenseValueObject = new ExpenseValueObject(
                expenseSourceDto.getDescription(),
                expenseSourceDto.getAmount(),
                expenseSourceDto.getLocation(),
                expenseSourceDto.getExpenseDate()
        );

        expenseManagementDomain.updateExpense(expenseValueObject, expenseById);

        // connection between user and expense already exists so we don't have to save user separately

        expenseCommandService.saveExpense(expenseById);
    }

    @Override
    public void deleteExpense(final Long id) {
        final UserDomainEntity loggedUser = authenticationService.getUserFromContext();
        final ExpenseDomainEntity expenseById = expenseCommandService.findExpenseById(id);
// its used to check if expense is in same group but u are not a owner (u are admin) - different response
        final UserDomainEntity userFromGivenIdExpense = userCommandService.findUserById(expenseById.getUserId());
        expenseValidator.validateAccessToExpense(loggedUser, userFromGivenIdExpense, expenseById);

        loggedUser.removeExpense(id);
        userCommandService.saveUser(loggedUser);
        expenseCommandService.deleteExpense(id);


    }

    private void prepareExpenseIfLocationIsNull(final ExpenseDto expenseDto, final String userIp) {
        if (expenseDto.getLocation() == null || expenseDto.getLocation().isEmpty()) {
            expenseGeolocationService.setLocationForExpenseFromUserIp(expenseDto, userIp);

        }
    }


}
