package com.patrykdankowski.financeflock.expense_category.port;

import com.patrykdankowski.financeflock.expense_category.model.entity.ExpenseCategoryDomainEntity;


public interface ExpenseCategoryCommandRepositoryPort {

    ExpenseCategoryDomainEntity save(ExpenseCategoryDomainEntity expenseCategoryDomainEntity);

}
