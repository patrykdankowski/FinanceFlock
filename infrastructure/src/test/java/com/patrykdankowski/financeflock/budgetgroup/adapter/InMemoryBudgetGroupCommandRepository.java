package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.budgetgroup.entity.BudgetGroupSqlEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryBudgetGroupCommandRepository {
    private final Map<Long, BudgetGroupSqlEntity> budgetGroupStore = new HashMap<>();
    private long currentId = 1L;

    public Optional<BudgetGroupSqlEntity> findById(Long id) {
        return Optional.ofNullable(budgetGroupStore.get(id));
    }

    public BudgetGroupSqlEntity save(BudgetGroupSqlEntity budgetGroupDomainEntity) {
        if (budgetGroupDomainEntity.getId() == null) {
            budgetGroupDomainEntity.setId(currentId++);
        }
        budgetGroupStore.put(budgetGroupDomainEntity.getId(), budgetGroupDomainEntity);
        return budgetGroupDomainEntity;
    }

    public void delete(BudgetGroupSqlEntity budgetGroupDomainEntity) {
        budgetGroupStore.remove(budgetGroupDomainEntity.getId());
    }
}


