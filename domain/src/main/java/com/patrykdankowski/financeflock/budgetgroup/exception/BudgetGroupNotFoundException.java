package com.patrykdankowski.financeflock.budgetgroup.exception;

import com.patrykdankowski.financeflock.common.AppConstants;
import lombok.Getter;

@Getter
public class BudgetGroupNotFoundException extends RuntimeException {
    private String details;

    public BudgetGroupNotFoundException(Long budgetGroupId) {
        super("Budget group with ID " + budgetGroupId + " not found");
        this.details = String.format(AppConstants.BUDGET_GROUP_NOT_FOUND, budgetGroupId);
    }

}
