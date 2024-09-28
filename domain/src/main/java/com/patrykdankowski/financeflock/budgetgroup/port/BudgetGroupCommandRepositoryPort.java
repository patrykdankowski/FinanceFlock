package com.patrykdankowski.financeflock.budgetgroup.port;

import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;

import java.util.Optional;

public interface BudgetGroupCommandRepositoryPort {

    Optional<BudgetGroupDomainEntity> findById(Long id);

    BudgetGroupDomainEntity save(BudgetGroupDomainEntity budgetGroupDomainEntity);

    void delete(BudgetGroupDomainEntity budgetGroupDomainEntity);

}
