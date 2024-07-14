package com.patrykdankowski.financeflock.expense.model.vo;


import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class ExpenseValueObject {

    private final String description;
    private final BigDecimal amount;
    private final String location;
    private final LocalDateTime expenseDate;

    public ExpenseValueObject(final String description, final BigDecimal amount, final String location, final LocalDateTime expenseDate) {
        this.description = description;
        this.amount = amount;
        this.location = location;
        this.expenseDate = expenseDate;
    }
}
