package com.patrykdankowski.financeflock.budgetgroup.exception;

import com.patrykdankowski.financeflock.common.AppConstants;
import lombok.Getter;

@Getter
public class MaxUserCountInBudgetGroupException extends RuntimeException {

    private String details;

    public MaxUserCountInBudgetGroupException() {
        this.details = String.format("You are only allowed to add up %d  users. Remove one of existing users first", AppConstants.MAX_BUDGET_GROUP_SIZE - 1);
    }
}
