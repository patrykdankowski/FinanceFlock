package com.patrykdankowski.financeflock.common;

import com.patrykdankowski.financeflock.budgetgroup.BudgetGroup;
import lombok.Getter;

@Getter
public class UserAndGroupUpdateResult<T> {
    private BudgetGroup budgetGroupEntity;
    private T source;

    public UserAndGroupUpdateResult(final BudgetGroup budgetGroupEntity, final T source) {
        this.budgetGroupEntity = budgetGroupEntity;
        this.source = source;
    }
}
