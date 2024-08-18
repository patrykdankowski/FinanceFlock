package com.patrykdankowski.financeflock.expense.model.entity;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class ExpenseDomainEntity {

    public static ExpenseDomainEntity buildExpense(Long id,
                                                   Long userId,
                                                   BigDecimal amount,
                                                   LocalDateTime expenseDate,
                                                   String description,
                                                   String location) {
        return new ExpenseDomainEntity(id,
                userId,
                amount,
                expenseDate,
                description,
                location);
    }


    private Long id;
    private Long userId;

    private BigDecimal amount;
    private LocalDateTime expenseDate;
    private String description;
    private String location;

    private ExpenseDomainEntity(Long id,
                               Long userId,
                               BigDecimal amount,
                               LocalDateTime expenseDate,
                               String description,
                               String location) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.description = description;
        this.location = location;
        if (expenseDate != null) {
            this.expenseDate = expenseDate;
        } else {
            this.expenseDate = LocalDateTime.now();
        }

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
}
