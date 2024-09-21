package com.patrykdankowski.financeflock.expense.port;

import com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;

public interface ExpenseValidatorPort {


    void validateAccessToExpense(UserDomainEntity loggedUser,
                                 ExpenseDomainEntity expenseDomainEntity);
}
