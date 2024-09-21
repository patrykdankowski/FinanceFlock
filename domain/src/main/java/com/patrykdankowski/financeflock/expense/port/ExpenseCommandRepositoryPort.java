package com.patrykdankowski.financeflock.expense.port;

import com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ExpenseCommandRepositoryPort {

    ExpenseDomainEntity save(ExpenseDomainEntity expenseDomainEntity);

    Optional<ExpenseDomainEntity> findById(Long id);

    void deleteById(Long id);

}
