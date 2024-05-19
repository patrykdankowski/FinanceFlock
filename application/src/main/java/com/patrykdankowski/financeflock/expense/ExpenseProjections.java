package com.patrykdankowski.financeflock.expense;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ExpenseProjections {
    String getDescription();
    BigDecimal getAmount();
    String getLocation();
    LocalDateTime getExpenseDate();
}
