package com.patrykdankowski.financeflock.expense_category.port;


import com.patrykdankowski.financeflock.expense_category.model.entity.ExpenseCategoryDomainEntity;

public interface ExpenseCategoryCommandServicePort {

    ExpenseCategoryDomainEntity saveExpenseCategory(ExpenseCategoryDomainEntity expenseCategory);


}
