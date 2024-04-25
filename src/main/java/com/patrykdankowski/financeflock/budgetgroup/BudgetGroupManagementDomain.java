package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.common.UserAndGroupUpdateResult;

public interface BudgetGroupManagementDomain {


    BudgetGroup createBudgetGroup(BudgetGroupRequest budgetGroupRequest);

    UserAndGroupUpdateResult closeBudgetGroup();



}
