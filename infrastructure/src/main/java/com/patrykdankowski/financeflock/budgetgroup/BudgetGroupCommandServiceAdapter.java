package com.patrykdankowski.financeflock.budgetgroup;

import org.springframework.stereotype.Service;

@Service
class BudgetGroupCommandServiceAdapter implements BudgetGroupCommandServicePort {

    private final BudgetGroupCommandRepository budgetGroupCommandRepository;

    BudgetGroupCommandServiceAdapter(final BudgetGroupCommandRepository budgetGroupCommandRepository) {
        this.budgetGroupCommandRepository = budgetGroupCommandRepository;
    }

    @Override
    public BudgetGroup saveBudgetGroup(final BudgetGroup budgetGroup) {
        return budgetGroupCommandRepository.save(budgetGroup);
    }

    @Override
    public void deleteBudgetGroup(final BudgetGroup budgetGroup) {
        budgetGroupCommandRepository.delete(budgetGroup);
    }


}
