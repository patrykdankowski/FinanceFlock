package com.patrykdankowski.financeflock.mapper;

import com.patrykdankowski.financeflock.expense.ExpenseSqlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseMapperRepository extends JpaRepository<ExpenseSqlEntity, Long> {
}
