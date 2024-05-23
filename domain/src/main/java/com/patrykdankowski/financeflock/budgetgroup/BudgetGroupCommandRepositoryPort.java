package com.patrykdankowski.financeflock.budgetgroup;

import java.util.Optional;

interface BudgetGroupCommandRepositoryPort {

    Optional<BudgetGroupDomainEntity> findById(long id);

    BudgetGroupDomainEntity save(BudgetGroupDomainEntity budgetGroupDomainEntity);

    void delete(BudgetGroupDomainEntity budgetGroupDomainEntity);

}
