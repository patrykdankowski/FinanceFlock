package com.patrykdankowski.financeflock.expense_category.exception;

import lombok.Getter;

@Getter
public class ExpenseCategoryNotFoundException extends RuntimeException {
    private Long id;

    public ExpenseCategoryNotFoundException(final Long id) {
        this.id = id;
    }
}
