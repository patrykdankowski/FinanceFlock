package com.patrykdankowski.financeflock.budgetgroup;


import java.util.Optional;

public interface BudgetGroupCommandServicePort {
    BudgetGroupDomainEntity saveBudgetGroup(BudgetGroupDomainEntity budgetGroupDomainEntity);

    void deleteBudgetGroup(BudgetGroupDomainEntity budgetGroupDomainEntity);

    BudgetGroupDomainEntity findBudgetGroupById(Long id);

}
