package com.patrykdankowski.financeflock.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetGroupDto {
    @Size(min = 5)
    @NotBlank
    private String description;


}
