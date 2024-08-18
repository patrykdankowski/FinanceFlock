package com.patrykdankowski.financeflock.expense.port;

import com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ExpenseFactoryPort {

    ExpenseDomainEntity createExpanseFromRequest(final Long id, final Long userId, final BigDecimal amount, final LocalDateTime expenseDate, final String description, final String location);
}
