package com.patrykdankowski.financeflock.mapper.expense;

import com.patrykdankowski.financeflock.expense.entity.ExpenseSqlEntity;
import com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity;
import com.patrykdankowski.financeflock.user.adapter.InMemoryUserQueryRepository;
import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpenseMapperWithCustomRepositoriesTest {


    private InMemoryUserQueryRepository inMemoryUserQueryRepository;
    private ExpenseMapperInMemory expenseMapper;

    @BeforeEach
    void setUp() {
        inMemoryUserQueryRepository = new InMemoryUserQueryRepository();
        expenseMapper = new ExpenseMapperInMemory(inMemoryUserQueryRepository);
    }

    @Test
    void toSqlEntity_shouldMapDomainEntityToSqlEntity() {
        Long userId = 1L;
        Long expenseId = 10L;

        UserSqlEntity user = new UserSqlEntity();
        user.setId(userId);
        user.setEmail("john.doe@example.com");
        inMemoryUserQueryRepository.save(user);

        ExpenseDomainEntity expenseDomainEntity = ExpenseDomainEntity.buildExpense(
                expenseId, userId, BigDecimal.valueOf(100.0), LocalDateTime.now(), "Dinner", "Restaurant");

        ExpenseSqlEntity result = expenseMapper.toSqlEntity(expenseDomainEntity);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(expenseId);
        assertThat(result.getAmount()).isEqualTo(BigDecimal.valueOf(100.0));
        assertThat(result.getDescription()).isEqualTo("Dinner");
        assertThat(result.getLocation()).isEqualTo("Restaurant");
        assertThat(result.getUser()).isEqualTo(user);
    }

    @Test
    void toSqlEntity_shouldReturnNull_whenDomainEntityIsNull() {
        ExpenseSqlEntity result = expenseMapper.toSqlEntity(null);

        assertThat(result).isNull();
    }

    @Test
    void toDomainEntity_shouldMapSqlEntityToDomainEntity() {
        Long userId = 1L;
        Long expenseId = 10L;

        UserSqlEntity user = new UserSqlEntity();
        user.setId(userId);
        inMemoryUserQueryRepository.save(user);

        ExpenseSqlEntity expenseSqlEntity = new ExpenseSqlEntity();
        expenseSqlEntity.setId(expenseId);
        expenseSqlEntity.setUser(user);
        expenseSqlEntity.setAmount(BigDecimal.valueOf(150.0));
        expenseSqlEntity.setDescription("Groceries");
        expenseSqlEntity.setLocation("Supermarket");
        expenseSqlEntity.setExpenseDate(LocalDateTime.now());

        ExpenseDomainEntity result = expenseMapper.toDomainEntity(expenseSqlEntity);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(expenseId);
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getAmount()).isEqualTo(BigDecimal.valueOf(150.0));
        assertThat(result.getDescription()).isEqualTo("Groceries");
        assertThat(result.getLocation()).isEqualTo("Supermarket");
    }

    @Test
    void toDomainEntity_shouldReturnNull_whenSqlEntityIsNull() {
        ExpenseDomainEntity result = expenseMapper.toDomainEntity(null);

        assertThat(result).isNull();
    }
}


