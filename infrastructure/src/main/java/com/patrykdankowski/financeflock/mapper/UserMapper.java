package com.patrykdankowski.financeflock.mapper;

import com.patrykdankowski.financeflock.user.UserDomainEntity;
import com.patrykdankowski.financeflock.user.UserSqlEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {BudgetGroupMapper.class, ExpenseMapper.class})
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "budgetGroup", ignore = true)
    @Mapping(target = "expenseList", ignore = true)
    UserDomainEntity toDomainEntity(UserSqlEntity source);

    @Mapping(target = "budgetGroup", ignore = true)
    @Mapping(target = "expenseList", ignore = true)
    UserSqlEntity toSqlEntity(UserDomainEntity source);


    @AfterMapping
    default void handleUserBudgetGroup(UserSqlEntity userSqlEntity, @MappingTarget UserDomainEntity userDomainEntity) {
        if (userSqlEntity.getBudgetGroup() != null) {
            userDomainEntity.setBudgetGroup(BudgetGroupMapper.INSTANCE.toDomainEntity(userSqlEntity.getBudgetGroup()));
        }
    }

    @AfterMapping
    default void handleUserBudgetGroup(UserDomainEntity userDomainEntity, @MappingTarget UserSqlEntity userSqlEntity) {
        if (userDomainEntity.getBudgetGroup() != null) {
            userSqlEntity.setBudgetGroup(BudgetGroupMapper.INSTANCE.toSqlEntity(userDomainEntity.getBudgetGroup()));
        }
    }

    @AfterMapping
    default void handleUserExpenses(UserSqlEntity userSqlEntity, @MappingTarget UserDomainEntity userDomainEntity) {
        if (userSqlEntity.getExpenseList() != null) {
            userDomainEntity.setExpenseList(userSqlEntity.getExpenseList().stream()
                    .map(ExpenseMapper.INSTANCE::toDomainEntity)
                    .collect(Collectors.toSet()));
        }
    }

    @AfterMapping
    default void handleUserExpenses(UserDomainEntity userDomainEntity, @MappingTarget UserSqlEntity userSqlEntity) {
        if (userDomainEntity.getExpenseList() != null) {
            userSqlEntity.setExpenseList(userDomainEntity.getExpenseList().stream()
                    .map(ExpenseMapper.INSTANCE::toSqlEntity)
                    .collect(Collectors.toSet()));
        }
    }

}


