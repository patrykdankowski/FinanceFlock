package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.expense.dto.ExpenseDto;
import com.patrykdankowski.financeflock.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "expanses")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Expense {

    public ExpenseDto toDtoLight() {
        return ExpenseDto.builder()
                .id(this.id)
                .amount(this.amount)
                .expenseDate(this.expenseDate)
                .description(this.description)
                .location(this.location)
                .ownerId(this.user.getId())
                .ownerGroupId(this.user.getBudgetGroup().getId())
                .build();
    }

    public static Expense fromDto(ExpenseDto dto) {
        return Expense.builder()
                .amount(dto.getAmount())
                .id(dto.getId())
                .expenseDate(dto.getExpenseDate())
                .user(User.fromDto(dto.getOwner()))
                .location(dto.getLocation())
                .description(dto.getDescription())
                .build();

    }


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
