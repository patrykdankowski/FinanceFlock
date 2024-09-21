package com.patrykdankowski.financeflock.expense.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class ExpenseUpdateDto {

    private String description;
    private BigDecimal amount;
    private String location;
    private LocalDateTime expenseDate;

}
