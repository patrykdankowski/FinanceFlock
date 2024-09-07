package com.patrykdankowski.financeflock.expense.model.vo;

import com.patrykdankowski.financeflock.expense.exception.ExpenseValidationException;

import java.math.BigDecimal;

public record AmountVO(BigDecimal value) {

    public AmountVO {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ExpenseValidationException("Expense amount must be greater than zero");
        }
        if (value.scale() > 2) {
            throw new ExpenseValidationException("Amount cannot have more than two decimal places");
        }
        if (value.precision() - value.scale() > 7) {
            throw new ExpenseValidationException("Amount cannot have more than seven digits before the decimal point");
        }
    }

    @Override
    public BigDecimal value() {
        return value;
    }
}
