package com.patrykdankowski.financeflock.budgetgroup;

import org.springframework.data.repository.Repository;

public interface BudgetGroupCommandRepositoryAdapter extends BudgetGroupCommandRepositoryPort, Repository<BudgetGroupDomainEntity,Long> {
}
