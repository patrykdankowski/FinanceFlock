package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.User;
import lombok.Getter;

@Getter
public class GroupUpdateResult<T> {
    private BudgetGroup budgetGroup;
    private T source;

    GroupUpdateResult(final BudgetGroup budgetGroup, final T source) {
        this.budgetGroup = budgetGroup;
        this.source = source;
    }
}
