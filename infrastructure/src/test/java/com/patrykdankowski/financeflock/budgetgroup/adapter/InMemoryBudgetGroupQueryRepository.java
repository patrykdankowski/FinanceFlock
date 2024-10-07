package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.budgetgroup.entity.BudgetGroupSqlEntity;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryBudgetGroupQueryRepository {

    private final Map<Long, BudgetGroupSqlEntity> budgetGroupStore = new HashMap<>();
    private long currentId = 1L;


    public Optional<BudgetGroupSqlEntity> findById(Long id) {
        return Optional.ofNullable(budgetGroupStore.get(id));
    }

    public BudgetGroupSqlEntity save(BudgetGroupSqlEntity budgetGroupSqlEntity) {
        if (budgetGroupSqlEntity.getId() == null) {
            budgetGroupSqlEntity.setId(currentId++);
        }
        budgetGroupStore.put(budgetGroupSqlEntity.getId(), budgetGroupSqlEntity);
        return budgetGroupSqlEntity;
    }

}
