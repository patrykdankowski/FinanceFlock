package com.patrykdankowski.financeflock.expense;

public interface ExpenseService {

    Expense saveExpense(Expense expense);
    Expense getExpenseById(long id);

}
