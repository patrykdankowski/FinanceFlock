package com.patrykdankowski.financeflock.budgetgroup.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BudgetGroupRequest {
    @Size(min = 5)
    @NotBlank
    private String description;

    public String getDescription() {
        return description;
    }
}
