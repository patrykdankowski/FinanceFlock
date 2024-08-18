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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "expanses")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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



    @Override
    public String toString() {
        return "ExpenseSqlEntity{" +
                "id=" + id +
                '}';
    }
}
