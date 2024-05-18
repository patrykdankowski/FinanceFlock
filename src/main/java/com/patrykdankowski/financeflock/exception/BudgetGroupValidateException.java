package com.patrykdankowski.financeflock.exception;

import lombok.Getter;

@Getter
public class BudgetGroupValidateException extends RuntimeException {
    private String message;

    public BudgetGroupValidateException(final String message) {
        this.message = message;
    }
}
