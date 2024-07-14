package com.patrykdankowski.financeflock.budgetgroup.port;

import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;

import java.util.Optional;

public interface BudgetGroupQueryRepositoryPort {

    Optional<BudgetGroupDomainEntity> findBudgetGroupById(long id);


}
