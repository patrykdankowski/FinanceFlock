package com.patrykdankowski.financeflock.expense.adapter;


import com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity;
import com.patrykdankowski.financeflock.expense.entity.ExpenseSqlEntity;
import com.patrykdankowski.financeflock.expense.port.ExpenseCommandRepositoryPort;
import com.patrykdankowski.financeflock.mapper.ExpenseMapper;
import org.springframework.data.repository.Repository;

import java.util.Optional;


 interface ExpenseCommandRepositoryAdapter extends Repository<ExpenseSqlEntity, Long> {

    ExpenseSqlEntity save(ExpenseSqlEntity expenseSqlEntity);

    Optional<ExpenseSqlEntity> findById(Long id);

    void deleteById(Long id);

}

@org.springframework.stereotype.Repository
class ExpenseCommandRepositoryImpl implements ExpenseCommandRepositoryPort {


    private final ExpenseCommandRepositoryAdapter expenseCommandRepositoryAdapter;
    private final ExpenseMapper mapper;

    ExpenseCommandRepositoryImpl(final ExpenseCommandRepositoryAdapter expenseCommandRepositoryAdapter, final ExpenseMapper mapper) {
        this.expenseCommandRepositoryAdapter = expenseCommandRepositoryAdapter;
        this.mapper = mapper;
    }

    @Override
    public ExpenseDomainEntity save(final ExpenseDomainEntity expenseDomainEntity) {
        ExpenseSqlEntity savedExpense = expenseCommandRepositoryAdapter.save(mapper.toSqlEntity(expenseDomainEntity));
        return mapper.toDomainEntity(savedExpense);

    }

    @Override
    public Optional<ExpenseDomainEntity> findById(final Long id) {
        return expenseCommandRepositoryAdapter.findById(id)
                .map(expense -> mapper.toDomainEntity(expense));
    }

    @Override
    public void deleteById(final Long id) {
        expenseCommandRepositoryAdapter.deleteById(id);

    }
}
