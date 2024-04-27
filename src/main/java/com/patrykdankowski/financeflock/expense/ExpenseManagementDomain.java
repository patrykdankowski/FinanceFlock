package com.patrykdankowski.financeflock.expense;

public interface ExpenseManagementDomain {
    Expense addExpense(ExpenseDto expenseDto, String userIp);

    Expense updateExpense(Long id, ExpenseDto expenseDto);
}
