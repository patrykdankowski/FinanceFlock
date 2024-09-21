package com.patrykdankowski.financeflock.mapper;

import com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity;
import com.patrykdankowski.financeflock.expense.entity.ExpenseSqlEntity;
import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Transactional
public class ExpenseMapper {

    private final EntityManager entityManager;

    public ExpenseMapper(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public ExpenseSqlEntity toSqlEntity(ExpenseDomainEntity expenseDomainEntity) {
        if (expenseDomainEntity == null) {
            return null;
        }

        ExpenseSqlEntity expenseSqlEntity = new ExpenseSqlEntity();
        expenseSqlEntity.setId(expenseDomainEntity.getId());
        expenseSqlEntity.setExpenseDate(expenseDomainEntity.getExpenseDate());
        expenseSqlEntity.setAmount(expenseDomainEntity.getAmount());
        expenseSqlEntity.setLocation(expenseDomainEntity.getLocation());
        expenseSqlEntity.setDescription(expenseDomainEntity.getDescription());

        if (expenseDomainEntity.getUserId() != null) {
            UserSqlEntity owner = entityManager.find(UserSqlEntity.class, expenseDomainEntity.getUserId());
            if (owner != null) {
                expenseSqlEntity.setUser(owner);
                owner.getExpenseList().add(expenseSqlEntity);
            } else {
                log.warn("Owner with id {} not found", expenseDomainEntity.getUserId());
            }
        }
        return expenseSqlEntity;
    }

    public ExpenseDomainEntity toDomainEntity(ExpenseSqlEntity expenseSqlEntity) {
        if (expenseSqlEntity == null) {
            return null;

        }
        return ExpenseDomainEntity.buildExpense(expenseSqlEntity.getId(),
                expenseSqlEntity.getUser().getId(),
                expenseSqlEntity.getAmount(),
                expenseSqlEntity.getExpenseDate(),
                expenseSqlEntity.getDescription(),
                expenseSqlEntity.getLocation());

    }
}