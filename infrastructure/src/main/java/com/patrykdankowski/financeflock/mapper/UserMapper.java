package com.patrykdankowski.financeflock.mapper;

import com.patrykdankowski.financeflock.budgetgroup.entity.BudgetGroupSqlEntity;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.expense.entity.ExpenseSqlEntity;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@Transactional
public class UserMapper {

    private final EntityManager entityManager;

    public UserMapper(EntityManager entityManager) {
        this.entityManager = entityManager;
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

        // Mapowanie grupy budżetowej
        if (userDomainEntity.getBudgetGroupId() != null) {
            BudgetGroupSqlEntity budgetGroupSql = entityManager.find(BudgetGroupSqlEntity.class, userDomainEntity.getBudgetGroupId());
            if (budgetGroupSql != null) {
                log.info("Budget group is not null");
                userSqlEntity.setBudgetGroup(budgetGroupSql);
            } else {
                log.warn("Budget group with id {} not found", userDomainEntity.getBudgetGroupId());
            }
        }

        // Mapowanie listy wydatków jednym zapytaniem
        if (userDomainEntity.getExpenseListId() != null && !userDomainEntity.getExpenseListId().isEmpty()) {
            // Pobranie wszystkich wydatków w jednym zapytaniu
            List<ExpenseSqlEntity> sqlExpenses = entityManager.createQuery(
                            "SELECT e FROM ExpenseSqlEntity e WHERE e.id IN :expenseIds", ExpenseSqlEntity.class)
                    .setParameter("expenseIds", userDomainEntity.getExpenseListId())
                    .getResultList();

            // Ustawienie użytkownika dla każdej encji wydatku
            sqlExpenses.forEach(expenseSql -> expenseSql.setUser(userSqlEntity));

            // Przypisanie wydatków do użytkownika
            userSqlEntity.setExpenseList(new HashSet<>(sqlExpenses));
        }

        log.info("Mapped to SQL entity: {}", userSqlEntity);
        return userSqlEntity;
    }

//    public UserSqlEntity toSqlEntity(UserDomainEntity userDomainEntity) {
//        if (userDomainEntity == null) {
//            return null;
//        }
//
//        UserSqlEntity userSqlEntity = new UserSqlEntity();
//        userSqlEntity.setId(userDomainEntity.getId());
//        userSqlEntity.setEmail(userDomainEntity.getEmail());
//        userSqlEntity.setPassword(userDomainEntity.getPassword());
//        userSqlEntity.setName(userDomainEntity.getName());
//        userSqlEntity.setLastLoggedInAt(userDomainEntity.getLastLoggedInAt());
//        userSqlEntity.setCreatedAt(userDomainEntity.getCreatedAt());
//        userSqlEntity.setRole(userDomainEntity.getRole());
//        userSqlEntity.setShareData(userDomainEntity.isShareData());
//        userSqlEntity.setLastToggledShareData(userDomainEntity.getLastToggledShareData());
//
//        if (userDomainEntity.getBudgetGroupId() != null) {
//            BudgetGroupSqlEntity budgetGroupSql = entityManager.find(BudgetGroupSqlEntity.class, userDomainEntity.getBudgetGroupId());
//            if (budgetGroupSql != null) {
//                log.info("Budget group is not null");
//                userSqlEntity.setBudgetGroup(budgetGroupSql);
//            } else {
//                log.warn("Budget group with id {} not found", userDomainEntity.getBudgetGroupId());
//            }
//        }
//        if (userDomainEntity.getExpenseListId() != null) {
//            Set<ExpenseSqlEntity> sqlExpenses = new HashSet<>();
//            sqlExpenses = userDomainEntity.getExpenseListId().stream()
//                    .map(expenseId -> {
//                        log.info("esa");
//                                ExpenseSqlEntity expenseSql = entityManager.find(ExpenseSqlEntity.class, expenseId);
//                                if (expenseSql != null) {
//                                    expenseSql.setUser(userSqlEntity);
//                                }
//                                return expenseSql;
//                            }
//                    ).collect(Collectors.toSet());
//
//            userSqlEntity.setExpenseList(sqlExpenses);
//        }
//
//        log.info("Mapped to SQL entity: {}", userSqlEntity);
//        return userSqlEntity;
//    }

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

            log.info("halo");
            userSqlEntity.getExpenseList().forEach(expense -> userDomainEntity.addExpense(expense.getId()));
        }

        log.info("Mapped to domain entity: {}", userDomainEntity);
        return userDomainEntity;
    }
}