package com.patrykdankowski.financeflock.expense.port;

import com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity;

public interface ExpenseCommandServicePort {

    ExpenseDomainEntity findExpenseById(final Long id);

    ExpenseDomainEntity saveExpense(ExpenseDomainEntity expenseDomainEntity);

    void deleteExpense(final Long id);



}
