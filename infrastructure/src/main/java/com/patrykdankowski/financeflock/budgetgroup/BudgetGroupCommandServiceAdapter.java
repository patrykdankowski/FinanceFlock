package com.patrykdankowski.financeflock.budgetgroup;

import org.springframework.stereotype.Service;

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


}
