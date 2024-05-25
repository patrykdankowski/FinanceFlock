package com.patrykdankowski.financeflock.mapper;

import com.patrykdankowski.financeflock.expense.ExpenseDomainEntity;
import com.patrykdankowski.financeflock.expense.ExpenseSqlEntity;
import com.patrykdankowski.financeflock.user.UserDomainEntity;
import com.patrykdankowski.financeflock.user.UserSqlEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ExpenseMapper {

    ExpenseMapper INSTANCE = Mappers.getMapper(ExpenseMapper.class);

    @Mapping(target = "user", ignore = true)
    ExpenseDomainEntity toDomainEntity(ExpenseSqlEntity expenseSqlEntity);

    @Mapping(target = "user", ignore = true)
    ExpenseSqlEntity toSqlEntity(ExpenseDomainEntity expenseDomainEntity);

    @AfterMapping
    default void handleOwnerOfExpense(ExpenseDomainEntity expenseDomainEntity, @MappingTarget ExpenseSqlEntity expenseSqlEntity) {

        final UserSqlEntity sqlEntity = UserMapper.INSTANCE.toSqlEntity(expenseDomainEntity.getUser());
        expenseSqlEntity.setUser(sqlEntity);

    }

    @AfterMapping
    default void handleOwnerOfExpense(ExpenseSqlEntity expenseSqlEntity, @MappingTarget ExpenseDomainEntity expenseDomainEntity) {

        final UserDomainEntity userDomainEntity = UserMapper.INSTANCE.toDomainEntity(expenseSqlEntity.getUser());
        expenseDomainEntity.setUser(userDomainEntity);

    }

}


