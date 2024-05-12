package com.patrykdankowski.financeflock.budgetgroup;

import java.util.Optional;

public interface BudgetGroupCommandService {
    BudgetGroup saveBudgetGroup(BudgetGroup budgetGroup);

    void deleteBudgetGroup(BudgetGroup budgetGroup);

}
