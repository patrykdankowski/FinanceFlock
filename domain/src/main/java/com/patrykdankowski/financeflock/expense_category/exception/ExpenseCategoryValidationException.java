package com.patrykdankowski.financeflock.expense_category.exception;

import lombok.Getter;

@Getter
public class ExpenseCategoryValidationException extends RuntimeException {
    private String message;

    public ExpenseCategoryValidationException(final String message) {
        this.message = message;
    }
}
