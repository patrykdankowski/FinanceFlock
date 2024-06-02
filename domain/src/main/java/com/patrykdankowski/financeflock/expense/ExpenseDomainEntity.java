package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.user.UserDomainEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDomainEntity {


    private Long id;
    private Long userId;

    private BigDecimal amount;
    private LocalDateTime expenseDate;

    private String description;

    private String location;

    public Long getId() {
        return id;
    }

   public void setId(final Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    BigDecimal getAmount() {
        return amount;
    }

    void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    LocalDateTime getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(final LocalDateTime expenseDate) {
        this.expenseDate = expenseDate;
    }

    String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    String getLocation() {
        return location;
    }

    void setLocation(final String location) {
        this.location = location;
    }
}
