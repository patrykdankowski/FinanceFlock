package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.user.UserDomainEntity;

interface ExpenseManagementDomainPort {
    ExpenseDomainEntity addExpense(ExpenseDtoWriteModel expenseDtoWriteModel,
                                   final UserDomainEntity userFromContext);


    void updateExpense(ExpenseDtoWriteModel expenseSourceDto,
                       ExpenseDomainEntity expenseDomainEntity,
                       UserDomainEntity userFromContext);
}
