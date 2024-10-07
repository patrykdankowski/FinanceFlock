package com.patrykdankowski.financeflock.expense.adapter;

import com.patrykdankowski.financeflock.expense.entity.ExpenseSqlEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class InMemoryExpenseCommandRepository {

    private final Map<Long, ExpenseSqlEntity> expenseStore = new HashMap<>();
    private long currentId = 1L;

    public ExpenseSqlEntity save(ExpenseSqlEntity expenseSqlEntity) {
        if (expenseSqlEntity.getId() == null) {
            expenseSqlEntity.setId(currentId++);
        }
        expenseStore.put(expenseSqlEntity.getId(), expenseSqlEntity);
        return expenseSqlEntity;
    }

    public Optional<ExpenseSqlEntity> findById(Long id) {
        return Optional.ofNullable(expenseStore.get(id));
    }

    public Set<ExpenseSqlEntity> findAllById(Set<Long> ids) {
        return ids.stream()
                .map(expenseStore::get)
                .filter(expense -> expense != null)
                .collect(Collectors.toSet());
    }

    public void deleteById(Long id) {
        expenseStore.remove(id);
    }
}
