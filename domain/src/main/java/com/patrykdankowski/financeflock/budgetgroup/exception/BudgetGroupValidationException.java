package com.patrykdankowski.financeflock.budgetgroup.exception;

import lombok.Getter;

@Getter
public class BudgetGroupValidationException extends RuntimeException {
    private String message;

    public BudgetGroupValidationException(final String message) {
        this.message = message;
    }
}
