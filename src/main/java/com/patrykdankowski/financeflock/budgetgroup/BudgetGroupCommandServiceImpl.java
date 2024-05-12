package com.patrykdankowski.financeflock.budgetgroup;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class BudgetGroupCommandServiceImpl implements BudgetGroupCommandService {

    private final BudgetGroupCommandRepository budgetGroupCommandRepository;

    BudgetGroupCommandServiceImpl(final BudgetGroupCommandRepository budgetGroupCommandRepository) {
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
