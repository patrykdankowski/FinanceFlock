package com.patrykdankowski.financeflock.budgetgroup;

import java.util.Optional;

 interface BudgetGroupQueryRepositoryPort {

    Optional<BudgetGroupDomainEntity> findBudgetGroupById(long id);


}
