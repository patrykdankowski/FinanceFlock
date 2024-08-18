package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.budgetgroup.exception.BudgetGroupNotFoundException;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupCommandRepositoryPort;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupCommandServicePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class BudgetGroupCommandServiceAdapter implements BudgetGroupCommandServicePort {

    private final BudgetGroupCommandRepositoryPort budgetGroupCommandRepository;

    BudgetGroupCommandServiceAdapter(final BudgetGroupCommandRepositoryPort budgetGroupCommandRepository) {
        this.budgetGroupCommandRepository = budgetGroupCommandRepository;
    }

    @Override
    public BudgetGroupDomainEntity saveBudgetGroup(final BudgetGroupDomainEntity budgetGroupDomainEntity) {
        log.info("grupa");
        return budgetGroupCommandRepository.save(budgetGroupDomainEntity);
    }

    @Override
    public void deleteBudgetGroup(final BudgetGroupDomainEntity budgetGroupDomainEntity) {
        budgetGroupCommandRepository.delete(budgetGroupDomainEntity);
    }

    @Override
    public BudgetGroupDomainEntity findBudgetGroupById(final Long id) {
        return budgetGroupCommandRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Could not find budget group with id {}", id);
                    return new BudgetGroupNotFoundException(id);

                });
    }


}
