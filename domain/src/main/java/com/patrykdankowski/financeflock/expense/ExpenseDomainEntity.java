package com.patrykdankowski.financeflock.expense;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public class ExpenseDomainEntity {


    private Long id;
    private Long userId;

    private BigDecimal amount;
    private LocalDateTime expenseDate;
    private String description;
    private String location;

    public ExpenseDomainEntity(Long id,
                               Long userId,
                               BigDecimal amount,
                               LocalDateTime expenseDate,
                               String description,
                               String location) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.expenseDate = expenseDate;
        this.description = description;
        this.location = location;
    }

    public void updateInfo(BigDecimal amount,
                           LocalDateTime expenseDate,
                           String description,
                           String location) {

        if (amount != null) {
            this.amount = amount;
        }
        if (expenseDate != null) {
            this.expenseDate = expenseDate;
        }
        if (description != null) {
            this.description = description;
        }
        if (location != null) {
            this.location = location;
        }
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getExpenseDate() {
        return expenseDate;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

}
