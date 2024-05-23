package com.patrykdankowski.financeflock.expense;

import org.springframework.data.repository.Repository;

public interface ExpenseQueryRepositoryAdapter extends ExpenseQueryRepositoryPort, Repository<ExpenseDomainEntity, Long> {
}
