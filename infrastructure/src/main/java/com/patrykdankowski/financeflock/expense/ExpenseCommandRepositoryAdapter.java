package com.patrykdankowski.financeflock.expense;


import org.springframework.data.repository.Repository;

import java.util.Optional;


public interface ExpenseCommandRepositoryAdapter extends Repository<ExpenseSqlEntity, Long> {

    ExpenseSqlEntity save(ExpenseSqlEntity expenseSqlEntity);

    Optional<ExpenseSqlEntity> findById(Long id);

}

@org.springframework.stereotype.Repository
class ExpenseCommandRepositoryImpl implements ExpenseCommandRepositoryPort {


//    private final ExpenseCommandRepositoryAdapter expenseCommandRepositoryAdapter;
//    private final ExpenseMapper mapper;

//    ExpenseCommandRepositoryImpl(final ExpenseCommandRepositoryAdapter expenseCommandRepositoryAdapter, final ExpenseMapper mapper) {
//        this.expenseCommandRepositoryAdapter = expenseCommandRepositoryAdapter;
//        this.mapper = mapper;
//    }

    @Override
    public ExpenseDomainEntity save(final ExpenseDomainEntity expenseDomainEntity) {
return null;
//        return mapper.toDomainEntity(expenseCommandRepositoryAdapter.save(mapper.toSqlEntity(expenseDomainEntity)));
    }

    @Override
    public Optional<ExpenseDomainEntity> findById(final Long id) {
//        return expenseCommandRepositoryAdapter.findById(id)
//                .map(expense -> mapper.toDomainEntity(expense));
    return null;
    }
}
