package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.budgetgroup.dto.BudgetGroupRequest;

public interface BudgetGroupFacade {

    void createBudgetGroup(BudgetGroupRequest budgetGroupRequest);

    void closeBudgetGroup(Long id);

    void addUserToGroup(final String email, final Long id);

    void removeUserFromGroup(final String email, final Long id);
}
