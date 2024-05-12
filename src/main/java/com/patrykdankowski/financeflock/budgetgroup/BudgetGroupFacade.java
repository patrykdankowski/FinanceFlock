package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.budgetgroup.dto.BudgetGroupRequest;

public interface BudgetGroupFacade {

    void createBudgetGroup(BudgetGroupRequest budgetGroupRequest);

    void closeBudgetGroup();

    void addUserToGroup(String email);

    void removeUserFromGroup(String email);
}
