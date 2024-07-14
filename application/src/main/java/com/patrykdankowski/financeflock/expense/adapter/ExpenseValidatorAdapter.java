package com.patrykdankowski.financeflock.expense.adapter;

import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity;
import com.patrykdankowski.financeflock.expense.exception.ExpenseNotBelongToUserException;
import com.patrykdankowski.financeflock.expense.exception.ExpenseNotFoundException;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupValidatorPort;
import com.patrykdankowski.financeflock.expense.port.ExpenseValidatorPort;
import com.patrykdankowski.financeflock.user.port.UserValidatorPort;
import org.springframework.stereotype.Component;

@Component
class ExpenseValidatorAdapter implements ExpenseValidatorPort {

    private final BudgetGroupValidatorPort budgetGroupValidator;
    private final UserValidatorPort userValidator;

    public ExpenseValidatorAdapter(final BudgetGroupValidatorPort budgetGroupValidator,
                                   final UserValidatorPort userValidator) {
        this.budgetGroupValidator = budgetGroupValidator;
        this.userValidator = userValidator;
    }

    @Override
    public boolean isExpenseOfLoggedUser(final ExpenseDomainEntity expense,
                                         final UserDomainEntity user) {
        return user.getExpenseListId().contains(expense.getId()) &&
                expense.getUserId().equals(user.getId());
    }

    @Override
    public void validateAccessToExpense(final UserDomainEntity loggedUser,
                                        final UserDomainEntity userFromGivenIdExpense,
                                        final ExpenseDomainEntity expenseDomainEntity) {
        boolean isExpenseOfUser = isExpenseOfLoggedUser(expenseDomainEntity, loggedUser);
        boolean isExpenseInSameUserGroup = budgetGroupValidator.belongsToSameBudgetGroup(loggedUser, userFromGivenIdExpense);
        boolean isAdmin = userValidator.hasGivenRole(loggedUser, Role.GROUP_ADMIN);

        //for group admin only - different exception
        if (isExpenseInSameUserGroup && !isExpenseOfUser && isAdmin) {
            throw new ExpenseNotBelongToUserException(userFromGivenIdExpense.getId(), expenseDomainEntity.getId());
        } else if (!isExpenseOfUser) {
            throw new ExpenseNotFoundException(expenseDomainEntity.getId());
        }
    }


}
