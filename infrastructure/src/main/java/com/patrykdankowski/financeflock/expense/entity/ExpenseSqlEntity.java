package com.patrykdankowski.financeflock.expense.entity;

import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "expanses")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseSqlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserSqlEntity user;

    // kategoria wydatku
    @Column(precision = 9, scale = 2)
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

    public UserSqlEntity getUser() {
        return user;
    }

    public void setUser(final UserSqlEntity user) {
        this.user = user;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(final BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(final LocalDateTime expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "ExpenseSqlEntity{" +
                "id=" + id +
                '}';
    }
}
