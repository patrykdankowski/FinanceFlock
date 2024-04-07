package com.patrykdankowski.financeflock.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ExpenseWriteModel {
    private BigDecimal amount;
    private LocalDateTime expenseDate;

    private String description;

    private String location;
}
