package com.patrykdankowski.financeflock.mapper.user;

import com.patrykdankowski.financeflock.budgetgroup.adapter.InMemoryBudgetGroupQueryRepository;
import com.patrykdankowski.financeflock.budgetgroup.entity.BudgetGroupSqlEntity;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.expense.adapter.InMemoryExpenseCommandRepository;
import com.patrykdankowski.financeflock.expense.entity.ExpenseSqlEntity;
import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperWithCustomRepositoriesTest {

    private InMemoryBudgetGroupQueryRepository inMemoryBudgetGroupRepository;
    private InMemoryExpenseCommandRepository inMemoryExpenseCommandRepository;
    private UserMapperInMemory userMapper;

    @BeforeEach
    void setUp() {
        inMemoryBudgetGroupRepository = new InMemoryBudgetGroupQueryRepository();
        inMemoryExpenseCommandRepository = new InMemoryExpenseCommandRepository();
        userMapper = new UserMapperInMemory(inMemoryBudgetGroupRepository, inMemoryExpenseCommandRepository);
    }

    @Test
    void toSqlEntity_shouldHandleMissingExpensesGracefully() {
        Long userId = 1L;
        Long budgetGroupId = 2L;
        List<Long> expenseIds = Arrays.asList(10L, 20L);

        BudgetGroupSqlEntity budgetGroup = new BudgetGroupSqlEntity();
        budgetGroup.setId(budgetGroupId);
        inMemoryBudgetGroupRepository.save(budgetGroup);

        UserDomainEntity userDomainEntity = UserDomainEntity.buildUser(userId, "John Doe", "password123", "john@example.com", LocalDateTime.now());
        userDomainEntity.manageGroupMembership(budgetGroupId, Role.USER);
        userDomainEntity.addExpense(expenseIds.get(0));
        userDomainEntity.addExpense(expenseIds.get(1));

        UserSqlEntity result = userMapper.toSqlEntity(userDomainEntity);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isEqualTo("john@example.com");
        assertThat(result.getBudgetGroup()).isEqualTo(budgetGroup);
        assertThat(result.getExpenseList()).isEmpty();
    }

    @Test
    void toSqlEntity_shouldHandleNullBudgetGroupGracefully() {
        Long userId = 1L;
        List<Long> expenseIds = Arrays.asList(10L, 20L);

        ExpenseSqlEntity expense1 = new ExpenseSqlEntity();
        expense1.setId(expenseIds.get(0));
        inMemoryExpenseCommandRepository.save(expense1);

        ExpenseSqlEntity expense2 = new ExpenseSqlEntity();
        expense2.setId(expenseIds.get(1));
        inMemoryExpenseCommandRepository.save(expense2);

        UserDomainEntity userDomainEntity = UserDomainEntity.buildUser(userId, "Jane Doe", "password123", "jane@example.com", LocalDateTime.now());
        userDomainEntity.addExpense(expenseIds.get(0));
        userDomainEntity.addExpense(expenseIds.get(1));

        UserSqlEntity result = userMapper.toSqlEntity(userDomainEntity);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getName()).isEqualTo("Jane Doe");
        assertThat(result.getEmail()).isEqualTo("jane@example.com");
        assertThat(result.getBudgetGroup()).isNull();
        assertThat(result.getExpenseList()).containsExactlyInAnyOrder(expense1, expense2);
    }

    @Test
    void toDomainEntity_shouldHandleMissingBudgetGroupGracefully() {
        Long userId = 1L;

        ExpenseSqlEntity expense1 = new ExpenseSqlEntity();
        expense1.setId(10L);
        inMemoryExpenseCommandRepository.save(expense1);

        ExpenseSqlEntity expense2 = new ExpenseSqlEntity();
        expense2.setId(20L);
        inMemoryExpenseCommandRepository.save(expense2);

        UserSqlEntity userSqlEntity = new UserSqlEntity();
        userSqlEntity.setId(userId);
        userSqlEntity.setName("John Smith");
        userSqlEntity.setEmail("john.smith@example.com");
        userSqlEntity.setBudgetGroup(null);
        userSqlEntity.setExpenseList(new HashSet<>(Arrays.asList(expense1, expense2)));
        userSqlEntity.setRole(Role.USER);

        UserDomainEntity result = userMapper.toDomainEntity(userSqlEntity);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getName()).isEqualTo("John Smith");
        assertThat(result.getEmail()).isEqualTo("john.smith@example.com");
        assertThat(result.getBudgetGroupId()).isNull();
        assertThat(result.getExpenseListId()).containsExactlyInAnyOrder(10L, 20L);
        assertThat(result.getRole()).isEqualTo(Role.USER);
    }
}
