package com.patrykdankowski.financeflock.expense.model.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseUpdateVO(String description, BigDecimal amount, String location, LocalDateTime expenseDate) {
}
