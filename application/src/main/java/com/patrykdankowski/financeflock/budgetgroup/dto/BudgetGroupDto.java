package com.patrykdankowski.financeflock.budgetgroup.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
@Getter
public class BudgetGroupDto {
    @Length(min = 5, max = 25)
    @NotBlank
    private String description;

}
