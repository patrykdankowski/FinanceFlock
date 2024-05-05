package com.patrykdankowski.financeflock.budgetgroup;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class BudgetGroupServiceImpl implements BudgetGroupService {

    private final BudgetGroupCommandRepository budgetGroupCommandRepository;

    BudgetGroupServiceImpl(final BudgetGroupCommandRepository budgetGroupCommandRepository) {
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

    @Override
    public Optional<BudgetGroup> findBudgetGroupById(final long id) {
        return budgetGroupCommandRepository.findById(id);
    }
}
