package com.patrykdankowski.financeflock.expense.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ExpenseCreateDto {

    @NotBlank
    private String description;
    private BigDecimal amount;
    private String location;
    private LocalDateTime expenseDate;

}
