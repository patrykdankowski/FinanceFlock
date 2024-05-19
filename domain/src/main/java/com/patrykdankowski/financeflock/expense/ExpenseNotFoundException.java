package com.patrykdankowski.financeflock.expense;

import lombok.Getter;

@Getter
public class ExpenseNotFoundException extends RuntimeException {
    private Long id;

    public ExpenseNotFoundException(final Long id) {
        this.id = id;
    }
}
