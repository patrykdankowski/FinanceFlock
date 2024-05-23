package com.patrykdankowski.financeflock.expense;

 interface ExpenseCommandServicePort {

    ExpenseDomainEntity retrieveExpenseById(final Long id);

}
