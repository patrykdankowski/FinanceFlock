package com.patrykdankowski.financeflock.mapper;

import com.patrykdankowski.financeflock.budgetgroup.BudgetGroupSqlEntity;
import com.patrykdankowski.financeflock.user.UserDomainEntity;
import com.patrykdankowski.financeflock.user.UserSqlEntity;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
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

        if (userDomainEntity.getBudgetGroupId() != null) {
            BudgetGroupSqlEntity budgetGroupSql = entityManager.find(BudgetGroupSqlEntity.class, userDomainEntity.getBudgetGroupId());
            if (budgetGroupSql != null) {
//                log.info("Budget group is not null");
                userSqlEntity.setBudgetGroup(budgetGroupSql);
            } else {
                log.warn("Budget group with id {} not found", userDomainEntity.getBudgetGroupId());
            }
        }
//        log.info("Mapped to SQL entity: {}", userSqlEntity);
        return userSqlEntity;
    }

    public UserDomainEntity toDomainEntity(UserSqlEntity userSqlEntity) {
        if (userSqlEntity == null) {
            return null;
        }

        UserDomainEntity userDomainEntity = new UserDomainEntity();
        userDomainEntity.setId(userSqlEntity.getId());
        userDomainEntity.setName(userSqlEntity.getName());
        userDomainEntity.setPassword(userSqlEntity.getPassword());
        userDomainEntity.setEmail(userSqlEntity.getEmail());
        userDomainEntity.setLastLoggedInAt(userSqlEntity.getLastLoggedInAt());
        userDomainEntity.setCreatedAt(userSqlEntity.getCreatedAt());
        userDomainEntity.setRole(userSqlEntity.getRole());
        userDomainEntity.setShareData(userSqlEntity.isShareData());

        if (userSqlEntity.getBudgetGroup() != null) {
            userDomainEntity.setBudgetGroupId(userSqlEntity.getBudgetGroup().getId());
        } else {
            log.info("Budget group is null for user: {}", userSqlEntity.getId());
        }
//        log.info("Mapped to domain entity: {}", userDomainEntity);
        return userDomainEntity;
    }
}
