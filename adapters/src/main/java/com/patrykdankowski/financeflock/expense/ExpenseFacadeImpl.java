package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.auth.AuthenticationServicePort;
import com.patrykdankowski.financeflock.user.UserCommandServicePort;
import com.patrykdankowski.financeflock.user.UserDomainEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExpenseFacadeImpl implements ExpenseFacade {

    ExpenseFacadeImpl(
            final ExpenseManagementDomainPort expenseManagementDomain,
            final ExpenseCommandRepositoryPort expenseCommandRepository,
            final AuthenticationServicePort authenticationService,
            final ExpenseCommandServicePort expenseCommandService,
            final ExpenseGeolocationServicePort expenseGeolocationService,
            final UserCommandServicePort userCommandService) {

        this.expenseManagementDomain = expenseManagementDomain;
        this.authenticationService = authenticationService;
        this.expenseCommandService = expenseCommandService;
        this.expenseGeolocationService = expenseGeolocationService;
        this.userCommandService = userCommandService;
    }


    private final ExpenseManagementDomainPort expenseManagementDomain;
    private final AuthenticationServicePort authenticationService;
    private final ExpenseCommandServicePort expenseCommandService;
    private final ExpenseGeolocationServicePort expenseGeolocationService;
    private final UserCommandServicePort userCommandService;

    @Override
    public Long addExpense(ExpenseDtoWriteModel expenseDtoWriteModel, String userIp) {

        final UserDomainEntity loggedUser = authenticationService.getUserFromContext();

        final ExpenseDtoWriteModel expenseDtoValidated = expenseGeolocationService.validateAndPrepareExpense(expenseDtoWriteModel,
                userIp);

        final ExpenseDomainEntity expenseDomainEntity = expenseManagementDomain.createExpense(expenseDtoValidated,
                loggedUser);

        final ExpenseDomainEntity savedExpense = expenseCommandService.saveExpense(expenseDomainEntity);

        loggedUser.addExpense(savedExpense.getId());

        // saving user must be after saving expense because we need expense id to connect expense with user
        userCommandService.saveUser(loggedUser);

        return savedExpense.getId();

    }

    @Override
    public void updateExpense(Long id, ExpenseDtoWriteModel expenseSourceDto) {
        final ExpenseDomainEntity expenseDomainEntity = expenseCommandService.findExpenseById(id);

        final UserDomainEntity loggedUser = authenticationService.getUserFromContext();

        // its used to check if expense is in same group but u are not a owner (u are admin) - different response
        final UserDomainEntity userFromGivenIdExpense = userCommandService.findUserById(expenseDomainEntity.getUserId());

        expenseManagementDomain.validateUserAccessToExpense(loggedUser, userFromGivenIdExpense, expenseDomainEntity);

        expenseManagementDomain.validateAndSetFieldsForExpense(expenseSourceDto, expenseDomainEntity);

        // connection between user and expense already exists so we don't have to save user separately

        expenseCommandService.saveExpense(expenseDomainEntity);
    }


}
