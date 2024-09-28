package com.patrykdankowski.financeflock.mapper;

import com.patrykdankowski.financeflock.budgetgroup.entity.BudgetGroupSqlEntity;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class BudgetGroupMapper {

    private final EntityManager entityManager;

    public BudgetGroupMapper(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public BudgetGroupSqlEntity toSqlEntity(BudgetGroupDomainEntity domainEntity) {
        if (domainEntity == null) {
            return null;
        }

        BudgetGroupSqlEntity budgetGroupSqlEntity = new BudgetGroupSqlEntity();
        budgetGroupSqlEntity.setId(domainEntity.getId());
        budgetGroupSqlEntity.setDescription(domainEntity.getDescription());

        if (domainEntity.getOwnerId() != null) {
            UserSqlEntity owner = entityManager.find(UserSqlEntity.class, domainEntity.getOwnerId());
            if (owner != null) {
                budgetGroupSqlEntity.setOwner(owner);
                owner.setBudgetGroup(budgetGroupSqlEntity);
            } else {
                log.warn("Owner with id {} not found", domainEntity.getOwnerId());
            }
            Set<UserSqlEntity> users = domainEntity.getListOfMembersId().stream().map(
                    userId -> {
                        UserSqlEntity userSqlEntity = entityManager.find(UserSqlEntity.class, userId);
                        if (userSqlEntity != null) {
                            userSqlEntity.setBudgetGroup(budgetGroupSqlEntity);
                            return userSqlEntity;
                        } else
                            log.warn("User not found");
                        return null;
                    }
            ).collect(Collectors.toSet());
            budgetGroupSqlEntity.setListOfMembers(users);

        }

        return budgetGroupSqlEntity;
    }


    public BudgetGroupDomainEntity toDomainEntity(BudgetGroupSqlEntity budgetGroupSql) {
        if (budgetGroupSql == null) {
            return null;
        }
        BudgetGroupDomainEntity domainEntity = BudgetGroupDomainEntity.buildBudgetGroup(budgetGroupSql.getId(),
                budgetGroupSql.getDescription(),
                budgetGroupSql.getOwner().getId());


        if (budgetGroupSql.getListOfMembers() != null) {
            budgetGroupSql.getListOfMembers().forEach(
                    member -> {
                        domainEntity.addUser(member.getId());
                    }
            );

        }
        return domainEntity;
    }
}
