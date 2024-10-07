package com.patrykdankowski.financeflock.mapper.expense;

import com.patrykdankowski.financeflock.expense.entity.ExpenseSqlEntity;
import com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity;
import com.patrykdankowski.financeflock.user.adapter.InMemoryUserQueryRepository;
import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;

public class ExpenseMapperInMemory {

    private final InMemoryUserQueryRepository inMemoryUserQueryRepository;

    public ExpenseMapperInMemory(InMemoryUserQueryRepository inMemoryUserQueryRepository) {
        this.inMemoryUserQueryRepository = inMemoryUserQueryRepository;
    }

    public ExpenseSqlEntity toSqlEntity(ExpenseDomainEntity expenseDomainEntity) {
        if (expenseDomainEntity == null) {
            return null;
        }

        ExpenseSqlEntity expenseSqlEntity = new ExpenseSqlEntity();
        expenseSqlEntity.setId(expenseDomainEntity.getId());
        expenseSqlEntity.setAmount(expenseDomainEntity.getAmount());
        expenseSqlEntity.setExpenseDate(expenseDomainEntity.getExpenseDate());
        expenseSqlEntity.setDescription(expenseDomainEntity.getDescription());
        expenseSqlEntity.setLocation(expenseDomainEntity.getLocation());

        if (expenseDomainEntity.getUserId() != null) {
            UserSqlEntity user = inMemoryUserQueryRepository.findById(expenseDomainEntity.getUserId())
                    .orElse(null);
            if (user != null) {
                expenseSqlEntity.setUser(user);
            }
        }

        return expenseSqlEntity;
    }

    public ExpenseDomainEntity toDomainEntity(ExpenseSqlEntity expenseSqlEntity) {
        if (expenseSqlEntity == null) {
            return null;
        }

        return ExpenseDomainEntity.buildExpense(
                expenseSqlEntity.getId(),
                expenseSqlEntity.getUser().getId(),
                expenseSqlEntity.getAmount(),
                expenseSqlEntity.getExpenseDate(),
                expenseSqlEntity.getDescription(),
                expenseSqlEntity.getLocation()
        );
    }
}
