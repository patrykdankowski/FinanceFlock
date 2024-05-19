package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.user.User;
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
public class Expense {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // kategoria wydatku
    @Column(precision = 9, scale = 2)
    private BigDecimal amount;
    private LocalDateTime expenseDate;

    private String description;

    private String location;

    public Long getId() {
        return id;
    }

    void setId(final Long id) {
        this.id = id;
    }

    User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
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

    void setExpenseDate(final LocalDateTime expenseDate) {
        this.expenseDate = expenseDate;
    }

    String getDescription() {
        return description;
    }

    void setDescription(final String description) {
        this.description = description;
    }

    String getLocation() {
        return location;
    }

    void setLocation(final String location) {
        this.location = location;
    }
}
