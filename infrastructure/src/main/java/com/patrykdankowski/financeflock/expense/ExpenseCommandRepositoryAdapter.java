package com.patrykdankowski.financeflock.expense;


import org.springframework.data.repository.Repository;

public interface ExpenseCommandRepositoryAdapter extends ExpenseCommandRepositoryPort, Repository<ExpenseDomainEntity, Long> {
}
