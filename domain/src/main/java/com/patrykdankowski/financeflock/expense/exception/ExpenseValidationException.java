package com.patrykdankowski.financeflock.expense.exception;

import lombok.Getter;

@Getter
public class ExpenseValidationException extends RuntimeException {
    private String message;

    public ExpenseValidationException(final String message) {
        this.message = message;
    }
}
