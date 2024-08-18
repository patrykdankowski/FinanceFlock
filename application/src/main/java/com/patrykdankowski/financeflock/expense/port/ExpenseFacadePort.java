package com.patrykdankowski.financeflock.expense.port;

import com.patrykdankowski.financeflock.expense.dto.ExpenseDto;
import jakarta.transaction.Transactional;

public interface ExpenseFacadePort {

    @Transactional
    Long createExpense(final ExpenseDto expenseDto, final String userIp);

    @Transactional
    void updateExpense(final Long id, final ExpenseDto expenseSourceDto);

    @Transactional
    void deleteExpense(final Long id);
}
