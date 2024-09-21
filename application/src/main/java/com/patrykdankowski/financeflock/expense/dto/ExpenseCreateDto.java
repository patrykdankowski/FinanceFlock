package com.patrykdankowski.financeflock.expense.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class ExpenseCreateDto {

    @NotBlank
    private String description;
    private BigDecimal amount;
    @NotBlank
    private String location;
    private LocalDateTime expenseDate;




    public String getDescription() {
        return description;
    }


    public BigDecimal getAmount() {
        return amount;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public LocalDateTime getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(final LocalDateTime expenseDate) {
        this.expenseDate = expenseDate;
    }
}
