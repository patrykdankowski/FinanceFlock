package com.patrykdankowski.financeflock.dto.projections;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface UserExpenseProjections {
    String getDescription();
    BigDecimal getAmount();
    String getLocation();
    LocalDateTime getExpenseDate();
}
