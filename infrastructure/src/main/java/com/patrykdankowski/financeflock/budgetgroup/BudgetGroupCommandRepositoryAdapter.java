package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.mapper.BudgetGroupMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.Repository;

import java.util.Optional;


public interface BudgetGroupCommandRepositoryAdapter extends Repository<BudgetGroupSqlEntity, Long> {

    Optional<BudgetGroupSqlEntity> findById(long id);

    BudgetGroupSqlEntity save(BudgetGroupSqlEntity budgetGroupDomainEntity);

    void delete(BudgetGroupSqlEntity budgetGroupDomainEntity);


}

@org.springframework.stereotype.Repository
@Slf4j
class BudgetGroupCommandRepositoryImpl implements BudgetGroupCommandRepositoryPort {

    private final BudgetGroupCommandRepositoryAdapter budgetGroupQueryRepositoryAdapter;
    private final BudgetGroupMapper mapper;
//    private final BudgetGroupMapperClass mapper;

    BudgetGroupCommandRepositoryImpl(final BudgetGroupCommandRepositoryAdapter budgetGroupQueryRepositoryAdapter,
                                     final BudgetGroupMapper mapper) {
        this.budgetGroupQueryRepositoryAdapter = budgetGroupQueryRepositoryAdapter;
        this.mapper = mapper;
    }


    @Override
    public Optional<BudgetGroupDomainEntity> findById(final long id) {

        return budgetGroupQueryRepositoryAdapter.findById(id)
                .map(group -> mapper.toDomainEntity(group));
    }

    @Override
    public BudgetGroupDomainEntity save(final BudgetGroupDomainEntity budgetGroupDomainEntity) {
        BudgetGroupSqlEntity entityToSaved = budgetGroupQueryRepositoryAdapter.save(mapper.toSqlEntity(budgetGroupDomainEntity));
        return mapper.toDomainEntity(entityToSaved);
    }

    @Override
    public void delete(final BudgetGroupDomainEntity budgetGroupDomainEntity) {

        budgetGroupQueryRepositoryAdapter.delete(mapper.toSqlEntity(budgetGroupDomainEntity));
    }
}