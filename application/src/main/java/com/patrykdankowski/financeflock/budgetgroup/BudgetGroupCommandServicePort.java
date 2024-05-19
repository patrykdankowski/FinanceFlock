package com.patrykdankowski.financeflock.budgetgroup;


public interface BudgetGroupCommandServicePort {
    BudgetGroup saveBudgetGroup(BudgetGroup budgetGroup);

    void deleteBudgetGroup(BudgetGroup budgetGroup);

}
