package com.patrykdankowski.financeflock.expense.port;

import com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;

public interface ExpenseValidatorPort {

    boolean isExpenseOfLoggedUser(ExpenseDomainEntity expense, UserDomainEntity user);

    void validateAccessToExpense(UserDomainEntity loggedUser,
                                 UserDomainEntity userFromGivenIdExpense,
                                 ExpenseDomainEntity expenseDomainEntity);
}
