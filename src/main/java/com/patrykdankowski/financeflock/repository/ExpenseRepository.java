package com.patrykdankowski.financeflock.repository;

import com.patrykdankowski.financeflock.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
