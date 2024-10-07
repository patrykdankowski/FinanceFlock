package com.patrykdankowski.financeflock.mapper.budgetgroup;

import com.patrykdankowski.financeflock.budgetgroup.adapter.InMemoryBudgetGroupQueryRepository;
import com.patrykdankowski.financeflock.budgetgroup.entity.BudgetGroupSqlEntity;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.user.adapter.InMemoryUserQueryRepository;
import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BudgetGroupMapperInMemory {

    private final InMemoryUserQueryRepository userRepository;

    public BudgetGroupMapperInMemory(InMemoryUserQueryRepository userRepository) {
        this.userRepository = userRepository;
    }

    public BudgetGroupSqlEntity toSqlEntity(BudgetGroupDomainEntity domainEntity) {
        if (domainEntity == null) {
            return null;
        }

        BudgetGroupSqlEntity budgetGroupSqlEntity = new BudgetGroupSqlEntity();
        budgetGroupSqlEntity.setId(domainEntity.getId());
        budgetGroupSqlEntity.setDescription(domainEntity.getDescription());

        if (domainEntity.getOwnerId() != null) {
            Optional<UserSqlEntity> owner = userRepository.findById(domainEntity.getOwnerId());
            if (owner.isPresent()) {
                budgetGroupSqlEntity.setOwner(owner.get());
                owner.get().setBudgetGroup(budgetGroupSqlEntity);
            } else {
                System.out.println("Owner with id " + domainEntity.getOwnerId() + " not found");
            }
        }

        Set<UserSqlEntity> users = domainEntity.getListOfMembersId().stream()
                .map(userRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .peek(userSqlEntity -> userSqlEntity.setBudgetGroup(budgetGroupSqlEntity))
                .collect(Collectors.toSet());

        budgetGroupSqlEntity.setListOfMembers(users);

        return budgetGroupSqlEntity;
    }

    public BudgetGroupDomainEntity toDomainEntity(BudgetGroupSqlEntity budgetGroupSql) {
        if (budgetGroupSql == null) {
            return null;
        }

        BudgetGroupDomainEntity domainEntity = BudgetGroupDomainEntity.buildBudgetGroup(
                budgetGroupSql.getId(),
                budgetGroupSql.getDescription(),
                budgetGroupSql.getOwner().getId()
        );

        if (budgetGroupSql.getListOfMembers() != null) {
            budgetGroupSql.getListOfMembers().forEach(
                    member -> domainEntity.addUser(member.getId())
            );
        }

        return domainEntity;
    }
}
