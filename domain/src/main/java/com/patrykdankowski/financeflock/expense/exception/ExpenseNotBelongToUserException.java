package com.patrykdankowski.financeflock.expense.exception;

import lombok.Getter;

@Getter
public class ExpenseNotBelongToUserException extends RuntimeException {
    private String name;
    private Long resourceId;
    private Long userId;

    public ExpenseNotBelongToUserException(String name, Long resourceId) {
        this.name = name;
        this.resourceId = resourceId;
    }
    public ExpenseNotBelongToUserException(Long userId, Long resourceId) {
        this.userId = userId;
        this.resourceId = resourceId;
    }

}
