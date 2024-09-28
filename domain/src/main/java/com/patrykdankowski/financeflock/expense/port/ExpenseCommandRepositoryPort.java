package com.patrykdankowski.financeflock.expense.port;

import com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity;

import java.util.Optional;


public interface ExpenseCommandRepositoryPort {

    ExpenseDomainEntity save(ExpenseDomainEntity expenseDomainEntity);

    Optional<ExpenseDomainEntity> findById(Long id);

    void deleteById(Long id);

}
