package com.patrykdankowski.financeflock.expense_category.adapter;

import com.patrykdankowski.financeflock.expense_category.entity.ExpenseCategorySqlEntity;
import com.patrykdankowski.financeflock.expense_category.model.entity.ExpenseCategoryDomainEntity;
import com.patrykdankowski.financeflock.expense_category.port.ExpenseCategoryCommandRepositoryPort;
import com.patrykdankowski.financeflock.mapper.ExpenseCategoryMapper;
import org.springframework.data.repository.Repository;

public interface ExpenseCategoryCommandRepositoryAdapter extends Repository<ExpenseCategorySqlEntity, Long> {

    ExpenseCategorySqlEntity save(ExpenseCategorySqlEntity expenseCategorySqlEntity);

}
@org.springframework.stereotype.Repository
class ExpenseCategoryCommandRepositoryImpl implements ExpenseCategoryCommandRepositoryPort{

    private final ExpenseCategoryCommandRepositoryAdapter expenseCategoryCommandRepository;
    private final ExpenseCategoryMapper mapper;

    ExpenseCategoryCommandRepositoryImpl(final ExpenseCategoryCommandRepositoryAdapter expenseCategoryCommandRepository, final ExpenseCategoryMapper mapper) {
        this.expenseCategoryCommandRepository = expenseCategoryCommandRepository;
        this.mapper = mapper;
    }

    @Override
    public ExpenseCategoryDomainEntity save(final ExpenseCategoryDomainEntity expenseCategoryDomainEntity) {

        ExpenseCategorySqlEntity expenseCategorySaved = expenseCategoryCommandRepository.save(mapper.toSqlEntity(expenseCategoryDomainEntity));


        return mapper.toDomainEntity(expenseCategorySaved);
    }
}
