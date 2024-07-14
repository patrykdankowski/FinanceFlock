package com.patrykdankowski.financeflock.budgetgroup.model.record;

import com.patrykdankowski.financeflock.budgetgroup.exception.BudgetGroupValidationException;

public record BudgetGroupDescription(String description) {

    public BudgetGroupDescription(String description) {
        if(description == null || description.isBlank() || description.length() < 5){
            throw new BudgetGroupValidationException("Budget group description must be at least 5 characters long ");
        }
        this.description = description;
    }

    public String getValue(){
        return description;
    }
}
