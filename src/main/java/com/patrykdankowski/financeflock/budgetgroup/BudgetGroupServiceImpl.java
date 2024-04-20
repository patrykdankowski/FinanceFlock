package com.patrykdankowski.financeflock.budgetgroup;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class BudgetGroupServiceImpl implements BudgetGroupService {

    private final BudgetGroupRepository budgetGroupRepository;

    BudgetGroupServiceImpl(final BudgetGroupRepository budgetGroupRepository) {
        this.budgetGroupRepository = budgetGroupRepository;
    }

    @Override
    public BudgetGroup saveBudgetGroup(final BudgetGroup budgetGroup) {
        return budgetGroupRepository.save(budgetGroup);
    }

    @Override
    public void deleteBudgetGroup(final BudgetGroup budgetGroup) {
        budgetGroupRepository.delete(budgetGroup);
    }

    @Override
    public Optional<BudgetGroup> findBudgetGroupById(final long id) {
        return budgetGroupRepository.findById(id);
    }
}
