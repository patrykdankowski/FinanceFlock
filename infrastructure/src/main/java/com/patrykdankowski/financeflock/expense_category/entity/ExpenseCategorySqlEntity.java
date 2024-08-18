package com.patrykdankowski.financeflock.expense_category.entity;

import com.patrykdankowski.financeflock.budgetgroup.entity.BudgetGroupSqlEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "expense_categories")
@Setter
@Getter
public class ExpenseCategorySqlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private BudgetGroupSqlEntity budgetGroup;
    private boolean defaultCategory;
}
