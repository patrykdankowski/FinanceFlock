package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.expense.dto.ExpenseDtoWriteModel;
import com.patrykdankowski.financeflock.user.User;

interface ExpenseManagementDomain {
    Expense addExpense(ExpenseDtoWriteModel expenseDtoWriteModel,
                       String userIp,
                       final User userFromContext);



    void updateExpense(ExpenseDtoWriteModel expenseSourceDto,
                       Expense expense,
                       User userFromContext);
}
