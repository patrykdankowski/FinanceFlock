package com.patrykdankowski.financeflock.mapper;

import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.entity.BudgetGroupSqlEntity;
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
                budgetGroupSqlEntity.setListOfMembers(Set.of(owner));
                owner.setBudgetGroup(budgetGroupSqlEntity);
            } else {
                log.warn("Owner with id {} not found", domainEntity.getOwnerId());
            }
        }
//        log.info("Mapped to SQL entity: {}", budgetGroupSqlEntity);

        return budgetGroupSqlEntity;
    }

    public BudgetGroupDomainEntity toDomainEntity(BudgetGroupSqlEntity budgetGroupSql) {
        if (budgetGroupSql == null) {
            return null;
        }

        Set<Long> ListOfIds = budgetGroupSql.getListOfMembers().stream().map(UserSqlEntity::getId).collect(Collectors.toSet());

        BudgetGroupDomainEntity domainEntity = new BudgetGroupDomainEntity(budgetGroupSql.getId(),
                budgetGroupSql.getDescription(),
                budgetGroupSql.getOwner().getId());

        domainEntity.updateListOfMembers(ListOfIds);


//        log.info("Mapped to domain entity: {}", domainEntity);
        return domainEntity;
    }
}
