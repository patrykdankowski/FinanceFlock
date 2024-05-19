package com.patrykdankowski.financeflock.expense;

 interface ExpenseCommandServicePort {

    Expense retrieveExpenseById(final Long id);

}
