package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.auth.AuthenticationServicePort;
import com.patrykdankowski.financeflock.user.UserCommandServicePort;
import com.patrykdankowski.financeflock.user.UserDomainEntity;
import com.patrykdankowski.financeflock.user.UserQueryRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExpenseFacadeImpl implements ExpenseFacade {

    ExpenseFacadeImpl(
            final ExpenseManagementDomainPort expenseManagementDomain,
//            final ExpenseCommandRepositoryPort expenseCommandRepository,
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
    public long addExpense(ExpenseDtoWriteModel expenseDtoWriteModel, String userIp) {

        final UserDomainEntity userFromContext = authenticationService.getUserFromContext();

        final ExpenseDtoWriteModel expenseDtoValidated = expenseGeolocationService.validateAndPrepareExpense(expenseDtoWriteModel,
                userIp);

        final ExpenseDomainEntity expenseDomainEntity = expenseManagementDomain.createExpense(expenseDtoValidated,
                userFromContext);

        final ExpenseDomainEntity savedExpense = expenseCommandService.saveExpense(expenseDomainEntity);

        userFromContext.addExpense(savedExpense.getId());

        // saving user must be after saving expense because we need expense id to connect expense with user
        userCommandService.saveUser(userFromContext);

        return savedExpense.getId();

    }

    @Override
    public void updateExpense(Long id, ExpenseDtoWriteModel expenseSourceDto) {

        final ExpenseDomainEntity expenseDomainEntity = expenseCommandService.findExpenseById(id);

        final UserDomainEntity userFromContext = authenticationService.getUserFromContext();

        final Long userGroupIdFromGivenExpenseId = userCommandService.findUserById(expenseDomainEntity.getUserId()).getBudgetGroupId();

        expenseManagementDomain.validateUserAccessToExpense(userFromContext, expenseDomainEntity, userGroupIdFromGivenExpenseId);

        expenseManagementDomain.validateAndSetFieldsForExpense(expenseSourceDto, expenseDomainEntity);

        // connection between user and expense already exists so we don't have to save user separately

        expenseCommandService.saveExpense(expenseDomainEntity);
    }


}
