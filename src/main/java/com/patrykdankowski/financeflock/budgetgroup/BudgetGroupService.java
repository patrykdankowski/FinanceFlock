package com.patrykdankowski.financeflock.budgetgroup;

import java.util.Optional;

public interface BudgetGroupService {
    BudgetGroup saveBudgetGroup(BudgetGroup budgetGroup);
    void deleteBudgetGroup(BudgetGroup budgetGroup);
    Optional<BudgetGroup> findBudgetGroupById(long id);
}
