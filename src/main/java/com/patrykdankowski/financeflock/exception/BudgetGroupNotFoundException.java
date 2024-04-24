package com.patrykdankowski.financeflock.exception;

import com.patrykdankowski.financeflock.constants.AppConstants;
import lombok.Getter;

@Getter
public class BudgetGroupNotFoundException extends RuntimeException {
    private String details;

    public BudgetGroupNotFoundException(Long budgetGroupId) {

        this.details = String.format(AppConstants.BUDGET_GROUP_NOT_FOUND, budgetGroupId);
    }

}
