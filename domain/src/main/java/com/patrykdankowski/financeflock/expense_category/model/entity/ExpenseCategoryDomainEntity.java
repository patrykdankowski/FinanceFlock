package com.patrykdankowski.financeflock.expense_category.model.entity;


import lombok.Getter;

@Getter
public class ExpenseCategoryDomainEntity {

    public static ExpenseCategoryDomainEntity buildExpenseCategory(final Long id, final String categoryName, final Long budgetGroupId, final boolean defaultCategory) {
        return new ExpenseCategoryDomainEntity(id, categoryName, budgetGroupId, defaultCategory);
    }

    private Long id;
    private String name;
    private Long budgetGroupId;
    private boolean defaultCategory;

    private ExpenseCategoryDomainEntity(final Long id, final String categoryName, final Long budgetGroupId, final boolean defaultCategory) {
        this.id = id;
        this.budgetGroupId = budgetGroupId;
        this.name = categoryName;
        this.defaultCategory = defaultCategory;
    }

    public void updateExpenseCategoryName(final String newMame) {

        if (!this.name.equals(newMame.trim())) {
            this.name = newMame;
            }
    }

}
