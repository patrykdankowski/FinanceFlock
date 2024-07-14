package com.patrykdankowski.financeflock.budgetgroup.port;


import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;

public interface BudgetGroupCommandServicePort {
    BudgetGroupDomainEntity saveBudgetGroup(BudgetGroupDomainEntity budgetGroupDomainEntity);

    void deleteBudgetGroup(BudgetGroupDomainEntity budgetGroupDomainEntity);

    BudgetGroupDomainEntity findBudgetGroupById(Long id);

}
