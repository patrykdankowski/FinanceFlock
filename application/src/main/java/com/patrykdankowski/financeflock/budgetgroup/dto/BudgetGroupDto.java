package com.patrykdankowski.financeflock.budgetgroup.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class BudgetGroupDto {
    @NotBlank
    private String description;

}
