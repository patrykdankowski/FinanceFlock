package com.patrykdankowski.financeflock.expense.adapter;

import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.expense.exception.ExpenseValidationException;
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

    @Override
    public void validateAccessToExpense(final UserDomainEntity loggedUser,
                                        final ExpenseDomainEntity expenseDomainEntity) {


        if (!loggedUser.getExpenseListId().contains(expenseDomainEntity.getId()) &&
                !expenseDomainEntity.getUserId().equals(expenseDomainEntity.getId())) ;
        throw new ExpenseValidationException("Cannot access expense");

    }
}
