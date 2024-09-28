package com.patrykdankowski.financeflock.expense.exception;

import com.patrykdankowski.financeflock.common.AppConstants;
import lombok.Getter;

@Getter
public class ExpenseNotFoundException extends RuntimeException {
    private String details;

    public ExpenseNotFoundException(final Long id) {
        this.details = String.format(AppConstants.EXPENSE_NOT_FOUND, id);
    }
}
