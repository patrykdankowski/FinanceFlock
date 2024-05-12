package com.patrykdankowski.financeflock.budgetgroup;


public interface BudgetGroupCommandService {
    BudgetGroup saveBudgetGroup(BudgetGroup budgetGroup);

    void deleteBudgetGroup(BudgetGroup budgetGroup);

}
