package com.patrykdankowski.financeflock.mapper;

import com.patrykdankowski.financeflock.expense.ExpenseDomainEntity;
import com.patrykdankowski.financeflock.expense.ExpenseSqlEntity;
import com.patrykdankowski.financeflock.user.UserNotFoundException;
import com.patrykdankowski.financeflock.user.UserSqlEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ExpenseMapper {


    @Mapping(target = "userId", ignore = true)
    ExpenseDomainEntity toDomainEntity(ExpenseSqlEntity expenseSqlEntity);

    @AfterMapping
    default void handleAfterMapping(ExpenseSqlEntity source,
                                        @MappingTarget ExpenseDomainEntity target) {
        target.setUserId(source.getUser().getId());
    }

    @Mapping(target = "user", ignore = true)
    ExpenseSqlEntity toSqlEntity(ExpenseDomainEntity expenseDomainEntity);

    @AfterMapping
    default void handleAfterMapping(ExpenseDomainEntity source,
                                        @MappingTarget ExpenseSqlEntity target,
                                        @Autowired UserMapperRepository userRepository) {
        var sourceUserId = source.getUserId();
        UserSqlEntity userSqlEntity = userRepository.findById(sourceUserId)
                .orElseThrow(() -> new UserNotFoundException(sourceUserId));

        target.setUser(userSqlEntity);

    }
}


