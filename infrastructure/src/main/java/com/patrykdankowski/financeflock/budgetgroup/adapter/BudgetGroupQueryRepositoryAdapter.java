package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.budgetgroup.entity.BudgetGroupSqlEntity;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupQueryRepositoryPort;
import com.patrykdankowski.financeflock.mapper.BudgetGroupMapper;
import org.springframework.data.repository.Repository;

import java.util.Optional;


public interface BudgetGroupQueryRepositoryAdapter extends Repository<BudgetGroupSqlEntity, Long> {

    Optional<BudgetGroupSqlEntity> findBudgetGroupById(long id);

}

@org.springframework.stereotype.Repository
class BudgetGroupQueryRepositoryImpl implements BudgetGroupQueryRepositoryPort {


    private final BudgetGroupQueryRepositoryAdapter budgetGroupQueryRepositoryAdapter;
    private final BudgetGroupMapper mapper;

    BudgetGroupQueryRepositoryImpl(final BudgetGroupQueryRepositoryAdapter budgetGroupQueryRepositoryAdapter,
                                   final BudgetGroupMapper mapper) {
        this.budgetGroupQueryRepositoryAdapter = budgetGroupQueryRepositoryAdapter;
        this.mapper = mapper;
    }

    public Optional<BudgetGroupDomainEntity> findBudgetGroupById(final long id) {

        return budgetGroupQueryRepositoryAdapter.findBudgetGroupById(id)
//                .map(group -> mapper.toDomainEntity(group));
                .map(group -> mapper.toDomainEntity(group));
    }
}