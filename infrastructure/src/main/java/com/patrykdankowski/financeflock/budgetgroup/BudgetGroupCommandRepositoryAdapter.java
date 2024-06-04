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

    private final BudgetGroupCommandRepositoryAdapter budgetGroupCommandRepository;
    private final BudgetGroupMapper mapper;
//    private final BudgetGroupMapperClass mapper;

    BudgetGroupCommandRepositoryImpl(final BudgetGroupCommandRepositoryAdapter budgetGroupCommandRepository,
                                     final BudgetGroupMapper mapper) {
        this.budgetGroupCommandRepository = budgetGroupCommandRepository;
        this.mapper = mapper;
    }


    @Override
    public Optional<BudgetGroupDomainEntity> findById(final long id) {

        return budgetGroupCommandRepository.findById(id)
                .map(group -> mapper.toDomainEntity(group));
    }

    @Override
    public BudgetGroupDomainEntity save(final BudgetGroupDomainEntity budgetGroupDomainEntity) {
        BudgetGroupSqlEntity entityToSaved = budgetGroupCommandRepository.save(mapper.toSqlEntity(budgetGroupDomainEntity));
        return mapper.toDomainEntity(entityToSaved);
    }

    @Override
    public void delete(final BudgetGroupDomainEntity budgetGroupDomainEntity) {

        log.info("before deleting {}", budgetGroupDomainEntity.getId());
        budgetGroupCommandRepository.delete(mapper.toSqlEntity(budgetGroupDomainEntity));
        log.info("after deleting");
    }
}