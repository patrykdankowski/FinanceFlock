package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.AppConstants;
import lombok.Getter;

@Getter
public class BudgetGroupNotFoundException extends RuntimeException {
    private String details;

    public BudgetGroupNotFoundException(Long budgetGroupId) {

        this.details = String.format(AppConstants.BUDGET_GROUP_NOT_FOUND, budgetGroupId);
    }

}
