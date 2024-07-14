package com.patrykdankowski.financeflock.expense.adapter;

import com.patrykdankowski.financeflock.expense.entity.ExpenseSqlEntity;
import com.patrykdankowski.financeflock.expense.port.ExpenseQueryRepositoryPort;
import org.springframework.data.repository.Repository;

public interface ExpenseQueryRepositoryAdapter extends ExpenseQueryRepositoryPort, Repository<ExpenseSqlEntity, Long> {
}
