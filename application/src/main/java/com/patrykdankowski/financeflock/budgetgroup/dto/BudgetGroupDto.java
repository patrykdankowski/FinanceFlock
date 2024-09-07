package com.patrykdankowski.financeflock.budgetgroup.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
@Getter
public class BudgetGroupDto {
    @NotBlank
    private String description;

}
