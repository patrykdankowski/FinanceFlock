package com.patrykdankowski.financeflock.expense.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ExpenseDto {

    private String description;
    private BigDecimal amount;
    private String location;
}
