package com.patrykdankowski.financeflock.budgetgroup;


public interface BudgetGroupCommandServicePort {
    BudgetGroupDomainEntity saveBudgetGroup(BudgetGroupDomainEntity budgetGroupDomainEntity);

    void deleteBudgetGroup(BudgetGroupDomainEntity budgetGroupDomainEntity);

}
