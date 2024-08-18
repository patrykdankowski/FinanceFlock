package com.patrykdankowski.financeflock.expense_category.port;

import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.expense_category.model.entity.ExpenseCategoryDomainEntity;
import com.patrykdankowski.financeflock.expense_category.model.record.ExpenseCategoryName;

public interface ExpenseCategoryManagementDomainPort {

    ExpenseCategoryDomainEntity createExpenseCategory(ExpenseCategoryName categoryName, Long budgetGroupId);
}
