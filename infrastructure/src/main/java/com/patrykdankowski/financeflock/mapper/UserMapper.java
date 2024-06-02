package com.patrykdankowski.financeflock.mapper;

import com.patrykdankowski.financeflock.budgetgroup.BudgetGroupNotFoundException;
import com.patrykdankowski.financeflock.budgetgroup.BudgetGroupSqlEntity;
import com.patrykdankowski.financeflock.expense.ExpenseSqlEntity;
import com.patrykdankowski.financeflock.user.UserDomainEntity;
import com.patrykdankowski.financeflock.user.UserSqlEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;
@Mapper(componentModel = "spring", uses = {BudgetGroupMapper.class, ExpenseMapper.class})
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "budgetGroupId", ignore = true)
    @Mapping(target = "expenseListId", ignore = true)
    UserDomainEntity toDomainEntity(UserSqlEntity userSqlEntity);

    @AfterMapping
    default void handleAfterMapping(UserSqlEntity source,
                                    @MappingTarget UserDomainEntity target) {

        if (source.getBudgetGroup() != null) {
            target.setBudgetGroupId(source.getId());

        }
        if (source.getExpenseList() != null) {
            Set<Long> expenseListIdAfterMapping = source.getExpenseList().stream()
                    .map(ExpenseSqlEntity::getId).collect(Collectors.toSet());

            target.setExpenseListId(expenseListIdAfterMapping);
        }

    }

    @Mapping(target = "budgetGroup", ignore = true)
    @Mapping(target = "expenseList", ignore = true)
    UserSqlEntity toSqlEntity(UserDomainEntity userDomainEntity);

    @AfterMapping
    default void handleAfterMapping(UserDomainEntity source,
                                    @MappingTarget UserSqlEntity target,
                                    @Autowired ExpenseMapperRepository expenseRepository,
                                    @Autowired BudgetGroupMapperRepository budgetGroupRepository) {
        Long budgetGroupId = source.getBudgetGroupId();

        if (budgetGroupId != null) {
            BudgetGroupSqlEntity budgetGroupSqlEntity = budgetGroupRepository.findById(budgetGroupId)
                    .orElseThrow(() -> new BudgetGroupNotFoundException(budgetGroupId));
            target.setBudgetGroup(budgetGroupSqlEntity);
        }

        Set<Long> expensesId = source.getExpenseListId();

        if (expensesId != null) {
            Set<ExpenseSqlEntity> expenseSqlEntities = expenseRepository.findAllById(expensesId).stream().collect(Collectors.toSet());

            target.setExpenseList(expenseSqlEntities);
        }
    }
}


