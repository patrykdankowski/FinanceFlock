package com.patrykdankowski.financeflock.budgetgroup;

public interface BudgetGroupManagementDomain {


    BudgetGroup createBudgetGroup(BudgetGroupRequest budgetGroupRequest);

    GroupUpdateResult closeBudgetGroup();



}
