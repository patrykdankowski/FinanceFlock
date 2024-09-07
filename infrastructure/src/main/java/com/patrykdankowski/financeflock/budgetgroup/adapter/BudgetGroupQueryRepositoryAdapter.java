package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.budgetgroup.entity.BudgetGroupSqlEntity;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupQueryRepositoryPort;
import com.patrykdankowski.financeflock.mapper.BudgetGroupMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;


public interface BudgetGroupQueryRepositoryAdapter extends Repository<BudgetGroupSqlEntity, Long> {

    @Query("SELECT bg FROM BudgetGroupSqlEntity bg JOIN FETCH bg.listOfMembers WHERE bg.id = :id")
    Optional<BudgetGroupSqlEntity> findBudgetGroupById(Long id);

}

@org.springframework.stereotype.Repository
@Slf4j
class BudgetGroupQueryRepositoryImpl implements BudgetGroupQueryRepositoryPort {


    private final BudgetGroupQueryRepositoryAdapter budgetGroupQueryRepositoryAdapter;
    private final BudgetGroupMapper mapper;

    BudgetGroupQueryRepositoryImpl(final BudgetGroupQueryRepositoryAdapter budgetGroupQueryRepositoryAdapter,
                                   final BudgetGroupMapper mapper) {
        this.budgetGroupQueryRepositoryAdapter = budgetGroupQueryRepositoryAdapter;
        this.mapper = mapper;
    }

    public Optional<BudgetGroupDomainEntity> findBudgetGroupById(final Long id) {
        log.info("before mapping rezpoturoium");
        final Optional<BudgetGroupDomainEntity> budgetGroupDomainEntity = budgetGroupQueryRepositoryAdapter.findBudgetGroupById(id)
//                .map(group -> mapper.toDomainEntity(group));
                .map(group -> mapper.toDomainEntity(group));
        log.info("after mapping repo");
        return budgetGroupDomainEntity;
    }
}