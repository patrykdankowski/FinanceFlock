package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.common.UserAndGroupUpdateResult;

 interface BudgetGroupManagementDomain {


    BudgetGroup createBudgetGroup(BudgetGroupRequest budgetGroupRequest);

    UserAndGroupUpdateResult closeBudgetGroup();



}
