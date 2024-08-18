package com.patrykdankowski.financeflock.mapper;

import com.patrykdankowski.financeflock.budgetgroup.entity.BudgetGroupSqlEntity;
import com.patrykdankowski.financeflock.expense_category.entity.ExpenseCategorySqlEntity;
import com.patrykdankowski.financeflock.expense_category.model.entity.ExpenseCategoryDomainEntity;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExpenseCategoryMapper {

    private final EntityManager entityManager;

    public ExpenseCategoryMapper(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public ExpenseCategorySqlEntity toSqlEntity(ExpenseCategoryDomainEntity expenseCategoryDomainEntity) {
        if (expenseCategoryDomainEntity == null) {
            return null;
        }

        ExpenseCategorySqlEntity expenseCategorySqlEntity = new ExpenseCategorySqlEntity();
        expenseCategorySqlEntity.setDefaultCategory(expenseCategoryDomainEntity.isDefaultCategory());
        expenseCategorySqlEntity.setId(expenseCategoryDomainEntity.getId());
        expenseCategorySqlEntity.setName(expenseCategoryDomainEntity.getName());

        if (expenseCategoryDomainEntity.getBudgetGroupId() != null) {
            BudgetGroupSqlEntity budgetGroup = entityManager.find(BudgetGroupSqlEntity.class, expenseCategoryDomainEntity.getBudgetGroupId());
            if (budgetGroup != null) {
                expenseCategorySqlEntity.setBudgetGroup(budgetGroup);
                budgetGroup.getListOfCategories().add(expenseCategorySqlEntity);
            } else {
                log.warn("Budget group with id {} not found", expenseCategoryDomainEntity.getBudgetGroupId());
            }
        }
        return expenseCategorySqlEntity;

    }

    public ExpenseCategoryDomainEntity toDomainEntity(ExpenseCategorySqlEntity expenseCategorySqlEntity) {
        if (expenseCategorySqlEntity == null) return null;
        return ExpenseCategoryDomainEntity.buildExpenseCategory(expenseCategorySqlEntity.getId(),
                expenseCategorySqlEntity.getName(),
                expenseCategorySqlEntity.getBudgetGroup().getId(),
                expenseCategorySqlEntity.isDefaultCategory());
    }
}