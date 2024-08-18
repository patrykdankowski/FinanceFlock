package com.patrykdankowski.financeflock.expense_category.port;

import com.patrykdankowski.financeflock.expense_category.model.entity.ExpenseCategoryDomainEntity;
import com.patrykdankowski.financeflock.expense_category.model.record.ExpenseCategoryName;

public interface ExpenseCategoryFactoryPort {

    ExpenseCategoryDomainEntity createExpenseCategoryFromRequest(final ExpenseCategoryName categoryName, final Long budgetGroupId);


}
