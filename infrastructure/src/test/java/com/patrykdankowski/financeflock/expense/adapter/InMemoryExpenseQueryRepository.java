package com.patrykdankowski.financeflock.expense.adapter;

import com.patrykdankowski.financeflock.expense.dto.ExpenseDto;
import com.patrykdankowski.financeflock.expense.entity.ExpenseSqlEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryExpenseQueryRepository {

    private final Map<Long, ExpenseSqlEntity> expenseStore = new HashMap<>();
    private long currentId = 1L;

    public ExpenseSqlEntity save(ExpenseSqlEntity expenseSqlEntity) {
        if (expenseSqlEntity.getId() == null) {
            expenseSqlEntity.setId(currentId++);
        }
        expenseStore.put(expenseSqlEntity.getId(), expenseSqlEntity);
        return expenseSqlEntity;
    }

    public List<ExpenseDto> findExpensesForUser(Long userId) {
        return expenseStore.values().stream()
                .filter(expense -> expense.getUser().getId().equals(userId))
                .map(expense -> new ExpenseDto(
                        expense.getDescription(),
                        expense.getAmount(),
                        expense.getLocation()
                ))
                .collect(Collectors.toList());
    }
}
