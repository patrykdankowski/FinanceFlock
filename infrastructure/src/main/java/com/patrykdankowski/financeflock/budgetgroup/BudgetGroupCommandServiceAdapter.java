package com.patrykdankowski.financeflock.budgetgroup;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class BudgetGroupCommandServiceAdapter implements BudgetGroupCommandServicePort {

    private final BudgetGroupCommandRepositoryPort budgetGroupCommandRepository;

    BudgetGroupCommandServiceAdapter(final BudgetGroupCommandRepositoryPort budgetGroupCommandRepository) {
        this.budgetGroupCommandRepository = budgetGroupCommandRepository;
    }

    @Override
    public BudgetGroupDomainEntity saveBudgetGroup(final BudgetGroupDomainEntity budgetGroupDomainEntity) {
        return budgetGroupCommandRepository.save(budgetGroupDomainEntity);
    }

    @Override
    public void deleteBudgetGroup(final BudgetGroupDomainEntity budgetGroupDomainEntity) {
        budgetGroupCommandRepository.delete(budgetGroupDomainEntity);
    }

    @Override
    public BudgetGroupDomainEntity findBudgetGroupById(final Long id) {
        return budgetGroupCommandRepository.findById(id)
                .orElseThrow(() -> new BudgetGroupNotFoundException(id));
    }


}
