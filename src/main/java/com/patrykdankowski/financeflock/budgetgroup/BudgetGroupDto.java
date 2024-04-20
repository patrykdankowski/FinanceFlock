package com.patrykdankowski.financeflock.budgetgroup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

 class BudgetGroupDto {
    @Size(min = 5)
    @NotBlank
    private String description;

    public String getDescription() {
        return description;
    }
}
