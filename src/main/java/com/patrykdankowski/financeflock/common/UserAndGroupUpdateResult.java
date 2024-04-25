package com.patrykdankowski.financeflock.common;

import com.patrykdankowski.financeflock.budgetgroup.BudgetGroup;
import lombok.Getter;

@Getter
public class UserAndGroupUpdateResult<T> {
    private BudgetGroup budgetGroup;
    private T source;

    public UserAndGroupUpdateResult(final BudgetGroup budgetGroup, final T source) {
        this.budgetGroup = budgetGroup;
        this.source = source;
    }
}
