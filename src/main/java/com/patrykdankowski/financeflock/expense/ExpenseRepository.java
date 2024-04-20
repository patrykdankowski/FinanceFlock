package com.patrykdankowski.financeflock.expense;

import org.springframework.data.jpa.repository.JpaRepository;

 interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
