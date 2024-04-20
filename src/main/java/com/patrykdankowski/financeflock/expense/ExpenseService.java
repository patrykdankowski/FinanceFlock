package com.patrykdankowski.financeflock.expense;

import java.util.Optional;

public interface ExpenseService {

    Expense saveExpense(Expense expense);
    Optional<Expense> findExpenseById(long id);

}
