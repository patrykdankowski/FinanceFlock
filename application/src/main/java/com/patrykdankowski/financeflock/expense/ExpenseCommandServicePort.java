package com.patrykdankowski.financeflock.expense;

 public interface ExpenseCommandServicePort {

    ExpenseDomainEntity findExpenseById(final Long id);

    ExpenseDomainEntity saveExpense(ExpenseDomainEntity expenseDomainEntity);

}
