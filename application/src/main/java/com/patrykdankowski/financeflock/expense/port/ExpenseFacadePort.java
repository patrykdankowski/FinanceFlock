package com.patrykdankowski.financeflock.expense.port;

import com.patrykdankowski.financeflock.expense.dto.ExpenseCreateDto;
import com.patrykdankowski.financeflock.expense.dto.ExpenseUpdateDto;
import jakarta.transaction.Transactional;

public interface ExpenseFacadePort {

    @Transactional
    Long createExpense(final ExpenseCreateDto expenseCreateDto, final String userIp);

    @Transactional
    void updateExpense(final Long id, final ExpenseUpdateDto expenseUpdateDto);

    @Transactional
    void deleteExpense(final Long id);
}
