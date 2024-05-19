package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.user.User;

interface ExpenseManagementDomainPort {
    Expense addExpense(ExpenseDtoWriteModel expenseDtoWriteModel,
                       final User userFromContext);


    void updateExpense(ExpenseDtoWriteModel expenseSourceDto,
                       Expense expense,
                       User userFromContext);
}
