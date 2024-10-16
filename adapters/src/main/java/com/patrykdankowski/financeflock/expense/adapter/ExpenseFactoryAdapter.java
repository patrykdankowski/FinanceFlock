package com.patrykdankowski.financeflock.expense.adapter;

import com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity;
import com.patrykdankowski.financeflock.expense.port.ExpenseFactoryPort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
class ExpenseFactoryAdapter implements ExpenseFactoryPort {
    @Override
    public ExpenseDomainEntity createExpanseFromRequest(final Long id, final Long userId, final BigDecimal amount, final LocalDateTime expenseDate, final String description, final String location) {

        return ExpenseDomainEntity.buildExpense(id, userId, amount, expenseDate, description, location);

    }
}
