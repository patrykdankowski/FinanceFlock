package com.patrykdankowski.financeflock.mapper.user;

import com.patrykdankowski.financeflock.budgetgroup.adapter.InMemoryBudgetGroupQueryRepository;
import com.patrykdankowski.financeflock.budgetgroup.entity.BudgetGroupSqlEntity;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.expense.adapter.InMemoryExpenseCommandRepository;
import com.patrykdankowski.financeflock.expense.entity.ExpenseSqlEntity;
import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class UserMapperInMemory {

    private final InMemoryBudgetGroupQueryRepository budgetGroupRepository;
    private final InMemoryExpenseCommandRepository expenseRepository;

    public UserMapperInMemory(final InMemoryBudgetGroupQueryRepository budgetGroupRepository,
                              final InMemoryExpenseCommandRepository expenseRepository) {
        this.budgetGroupRepository = budgetGroupRepository;
        this.expenseRepository = expenseRepository;
    }


    public UserSqlEntity toSqlEntity(UserDomainEntity userDomainEntity) {
        if (userDomainEntity == null) {
            return null;
        }

        UserSqlEntity userSqlEntity = new UserSqlEntity();
        userSqlEntity.setId(userDomainEntity.getId());
        userSqlEntity.setEmail(userDomainEntity.getEmail());
        userSqlEntity.setPassword(userDomainEntity.getPassword());
        userSqlEntity.setName(userDomainEntity.getName());
        userSqlEntity.setLastLoggedInAt(userDomainEntity.getLastLoggedInAt());
        userSqlEntity.setCreatedAt(userDomainEntity.getCreatedAt());
        userSqlEntity.setRole(userDomainEntity.getRole());
        userSqlEntity.setShareData(userDomainEntity.isShareData());
        userSqlEntity.setLastToggledShareData(userDomainEntity.getLastToggledShareData());

        if (userDomainEntity.getBudgetGroupId() != null) {
            Optional<BudgetGroupSqlEntity> budgetGroupOptional = budgetGroupRepository.findById(userDomainEntity.getBudgetGroupId());
            if (budgetGroupOptional.isPresent()) {
                userSqlEntity.setBudgetGroup(budgetGroupOptional.get());
            } else {
                log.warn("Budget group with id {} not found", userDomainEntity.getBudgetGroupId());
            }
        }

        if (userDomainEntity.getExpenseListId() != null && !userDomainEntity.getExpenseListId().isEmpty()) {
            Set<ExpenseSqlEntity> sqlExpenses = expenseRepository.findAllById(userDomainEntity.getExpenseListId());
            sqlExpenses.forEach(expenseSql -> expenseSql.setUser(userSqlEntity));
            userSqlEntity.setExpenseList(new HashSet<>(sqlExpenses));
        }

        return userSqlEntity;
    }

    public UserDomainEntity toDomainEntity(UserSqlEntity userSqlEntity) {
        if (userSqlEntity == null) {
            return null;
        }

        UserDomainEntity userDomainEntity = UserDomainEntity.buildUser(userSqlEntity.getId(),
                userSqlEntity.getName(),
                userSqlEntity.getPassword(),
                userSqlEntity.getEmail(),
                userSqlEntity.getCreatedAt());

        userDomainEntity.updateInfo(userSqlEntity.isShareData(),
                userSqlEntity.getLastToggledShareData(),
                userSqlEntity.getLastLoggedInAt());

        if (userSqlEntity.getBudgetGroup() != null) {
            userDomainEntity.manageGroupMembership(userSqlEntity.getBudgetGroup().getId(), userSqlEntity.getRole());
        } else {
            userDomainEntity.manageGroupMembership(null, Role.USER);
            log.info("Budget group is null for user: {}", userSqlEntity.getId());
        }

        if (userSqlEntity.getExpenseList() != null) {
            userSqlEntity.getExpenseList().forEach(expense -> userDomainEntity.addExpense(expense.getId()));
        }

        return userDomainEntity;
    }
}
