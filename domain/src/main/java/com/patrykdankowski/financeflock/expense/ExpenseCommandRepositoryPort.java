package com.patrykdankowski.financeflock.expense;

import java.util.Optional;


interface ExpenseCommandRepositoryPort {

    ExpenseDomainEntity save(ExpenseDomainEntity expenseDomainEntity);

    Optional<ExpenseDomainEntity> findById(Long id);

}
