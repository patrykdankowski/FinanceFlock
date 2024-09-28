package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.budgetgroup.entity.BudgetGroupSqlEntity;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupCommandRepositoryPort;
import com.patrykdankowski.financeflock.mapper.BudgetGroupMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;


 interface BudgetGroupCommandRepositoryAdapter extends Repository<BudgetGroupSqlEntity, Long> {

    @Query("SELECT u FROM BudgetGroupSqlEntity u " +
            "LEFT JOIN FETCH u.listOfMembers " +
            "WHERE u.id = :id")
    Optional<BudgetGroupSqlEntity> findById(Long id);

    BudgetGroupSqlEntity save(BudgetGroupSqlEntity budgetGroupDomainEntity);

    void delete(BudgetGroupSqlEntity budgetGroupDomainEntity);


}

@org.springframework.stereotype.Repository
@Slf4j
class BudgetGroupCommandRepositoryImpl implements BudgetGroupCommandRepositoryPort {

    private final BudgetGroupCommandRepositoryAdapter budgetGroupCommandRepository;
    private final BudgetGroupMapper mapper;

    BudgetGroupCommandRepositoryImpl(final BudgetGroupCommandRepositoryAdapter budgetGroupCommandRepository,
                                     final BudgetGroupMapper mapper) {
        this.budgetGroupCommandRepository = budgetGroupCommandRepository;
        this.mapper = mapper;
    }


    @Override
    public Optional<BudgetGroupDomainEntity> findById(final Long id) {

        return budgetGroupCommandRepository.findById(id)
                .map(group -> mapper.toDomainEntity(group));
    }

    @Override
    public BudgetGroupDomainEntity save(final BudgetGroupDomainEntity budgetGroupDomainEntity) {
        BudgetGroupSqlEntity entitySaved = budgetGroupCommandRepository.save(mapper.toSqlEntity(budgetGroupDomainEntity));
        return mapper.toDomainEntity(entitySaved);
    }

    @Override
    public void delete(final BudgetGroupDomainEntity budgetGroupDomainEntity) {

        budgetGroupCommandRepository.delete(mapper.toSqlEntity(budgetGroupDomainEntity));
    }
}