package com.patrykdankowski.financeflock.expense_category.model.record;


import com.patrykdankowski.financeflock.expense_category.exception.ExpenseCategoryValidationException;

public record ExpenseCategoryName(String categoryName) {

    public ExpenseCategoryName(String categoryName) {
        if(categoryName == null || categoryName.isBlank() || categoryName.length() < 3 || categoryName.length() > 25) {
            throw new ExpenseCategoryValidationException("Expense category name must be at least 3 characters length and cannot be longer than 25 characters");
        }
        this.categoryName = categoryName;
    }
}
