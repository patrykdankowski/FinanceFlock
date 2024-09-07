package com.patrykdankowski.financeflock.expense.model.vo;


import com.patrykdankowski.financeflock.expense.exception.ExpenseValidationException;

import java.time.LocalDateTime;

public record ExpenseCreateVO(String description, AmountVO amountVO, String location, LocalDateTime expenseDate) {


    public ExpenseCreateVO(final String description,
                           final AmountVO amountVO,
                           final String location,
                           final LocalDateTime expenseDate) {

        if (description.length() < 5 || description.length() > 25) {
            throw new ExpenseValidationException("Expense description name must be at least 5 characters length and cannot be longer than 25 characters");
        }

        if (expenseDate == null) {
            this.expenseDate = LocalDateTime.now();
        } else {
            this.expenseDate = expenseDate;
        }

        this.description = description;
        this.amountVO = amountVO;
        this.location = location;
    }
}
