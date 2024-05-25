package com.patrykdankowski.financeflock.mapper;

import com.patrykdankowski.financeflock.budgetgroup.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.BudgetGroupSqlEntity;
import com.patrykdankowski.financeflock.user.UserDomainEntity;
import com.patrykdankowski.financeflock.user.UserSqlEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface BudgetGroupMapper {

    BudgetGroupMapper INSTANCE = Mappers.getMapper(BudgetGroupMapper.class);

    @Mapping(target = "listOfMembers", ignore = true)
    @Mapping(target = "owner", ignore = true)
    BudgetGroupDomainEntity toDomainEntity(BudgetGroupSqlEntity budgetGroupSqlEntity);

    @Mapping(target = "listOfMembers", ignore = true)
    @Mapping(target = "owner", ignore = true)
    BudgetGroupSqlEntity toSqlEntity(BudgetGroupDomainEntity budgetGroupDomainEntity);

    @AfterMapping
    default void handleBudgetGroupMembers(BudgetGroupDomainEntity budgetGroupDomainEntity, @MappingTarget BudgetGroupSqlEntity budgetGroupSqlEntity) {

        if (budgetGroupDomainEntity.getListOfMembers() != null) {

            Set<UserSqlEntity> members = budgetGroupDomainEntity.getListOfMembers().stream()
                    .map(user -> {
                        UserSqlEntity userSql = UserMapper.INSTANCE.toSqlEntity(user);
                        return userSql;
                    }).collect(Collectors.toSet());
            budgetGroupSqlEntity.setListOfMembers(members);
        }
    }

    @AfterMapping
    default void handleBudgetGroupMembers(BudgetGroupSqlEntity budgetGroupSqlEntity, @MappingTarget BudgetGroupDomainEntity budgetGroupDomainEntity) {

        if (budgetGroupSqlEntity.getListOfMembers() != null) {
            Set<UserDomainEntity> members = budgetGroupSqlEntity.getListOfMembers().stream()
                    .map(user -> {
                        UserDomainEntity userDomain = UserMapper.INSTANCE.toDomainEntity(user);
                        return userDomain;
                    }).collect(Collectors.toSet());

            budgetGroupDomainEntity.setListOfMembers(members);

        }

    }

    @AfterMapping
    default void handleOwnerOfBudgetGroup(BudgetGroupDomainEntity budgetGroupDomainEntity, @MappingTarget BudgetGroupSqlEntity budgetGroupSqlEntity) {

        UserSqlEntity owner = UserMapper.INSTANCE.toSqlEntity(budgetGroupDomainEntity.getOwner());
        budgetGroupSqlEntity.setOwner(owner);

    }

    @AfterMapping
    default void handleOwnerOfBudgetGroup(BudgetGroupSqlEntity budgetGroupSql, @MappingTarget BudgetGroupDomainEntity budgetGroupDomainEntity) {

        UserDomainEntity owner = UserMapper.INSTANCE.toDomainEntity(budgetGroupSql.getOwner());
        budgetGroupDomainEntity.setOwner(owner);

    }

}


