package com.patrykdankowski.financeflock.budgetgroup;

import org.springframework.data.repository.Repository;

public interface BudgetGroupCommandQueryAdapter extends BudgetGroupQueryRepositoryPort, Repository<BudgetGroupDomainEntity,Long> {
}
