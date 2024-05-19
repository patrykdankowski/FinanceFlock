package com.patrykdankowski.financeflock.expense;

import org.springframework.data.repository.Repository;

import java.util.Optional;


interface ExpenseCommandRepository extends Repository<Expense, Long> {

    Expense save(Expense expense);

    Optional<Expense> findById(Long id);

}
