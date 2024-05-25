package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.user.UserSqlEntity;
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

//    public static ExpenseSqlEntity fromDomainExpense(ExpenseDomainEntity expenseDomainEntity) {
//        var result = new ExpenseSqlEntity();
//        result.amount = expenseDomainEntity.getAmount();
//        result.description = expenseDomainEntity.getDescription();
//        result.id = expenseDomainEntity.getId();
//        result.location = expenseDomainEntity.getLocation();
//        result.user = fromDomainUser(expenseDomainEntity.getUser());
//        return result;
//    }
//
//    public static ExpenseDomainEntity toDomainExpense(ExpenseSqlEntity expenseSqlEntity){
//        var result = new ExpenseDomainEntity();
//        result.setAmount(expenseSqlEntity.getAmount());
//        result.setId(expenseSqlEntity.getId());
//        result.setLocation(expenseSqlEntity.getLocation());
//        result.setExpenseDate(expenseSqlEntity.getExpenseDate());
//        result.setUser(toDomainUser((expenseSqlEntity.getUser())));
//        return result;
//    }


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

    void setId(final Long id) {
        this.id = id;
    }

    public UserSqlEntity getUser() {
        return user;
    }

    public void setUser(final UserSqlEntity user) {
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
