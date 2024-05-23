package com.patrykdankowski.financeflock.budgetgroup;

 interface BudgetGroupFacade {

    Long createBudgetGroup(BudgetGroupRequest budgetGroupRequest);

    void closeBudgetGroup(Long id);

    void addUserToGroup(final String email, final Long id);

    void removeUserFromGroup(final String email, final Long id);
}
