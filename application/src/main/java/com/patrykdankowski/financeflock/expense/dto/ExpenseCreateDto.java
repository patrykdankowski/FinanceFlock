package com.patrykdankowski.financeflock.expense.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ExpenseCreateDto {

    @NotBlank
    private String description;
    private BigDecimal amount;
    private String location;
    private LocalDateTime expenseDate;

    public void setLocation(final String location) {
        this.location = location;
    }
}
