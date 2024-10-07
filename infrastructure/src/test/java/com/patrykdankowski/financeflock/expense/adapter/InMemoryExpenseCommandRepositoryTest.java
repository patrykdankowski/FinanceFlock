package com.patrykdankowski.financeflock.expense.adapter;

import com.patrykdankowski.financeflock.expense.entity.ExpenseSqlEntity;
import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryExpenseCommandRepositoryTest {

    private InMemoryExpenseCommandRepository inMemoryExpenseCommandRepository;

    @BeforeEach
    void setUp() {
        inMemoryExpenseCommandRepository = new InMemoryExpenseCommandRepository();
    }

    @Test
    void save_shouldPersistExpenseSqlEntity() {
        UserSqlEntity user = new UserSqlEntity();
        user.setId(1L);
        user.setEmail("testuser@example.com");

        ExpenseSqlEntity expense = new ExpenseSqlEntity();
        expense.setUser(user);
        expense.setAmount(BigDecimal.valueOf(100.50));
        expense.setExpenseDate(LocalDateTime.now());
        expense.setDescription("Groceries");
        expense.setLocation("Berlin");

        ExpenseSqlEntity savedExpense = inMemoryExpenseCommandRepository.save(expense);

        assertThat(savedExpense).isNotNull();
        assertThat(savedExpense.getId()).isNotNull();
        assertThat(savedExpense.getUser()).isEqualTo(user);
        assertThat(savedExpense.getAmount()).isEqualTo(BigDecimal.valueOf(100.50));
        assertThat(savedExpense.getDescription()).isEqualTo("Groceries");
        assertThat(savedExpense.getLocation()).isEqualTo("Berlin");
    }

    @Test
    void findById_shouldReturnExpenseSqlEntity_whenExpenseExists() {
        UserSqlEntity user = new UserSqlEntity();
        user.setId(1L);
        user.setEmail("testuser@example.com");

        ExpenseSqlEntity expense = new ExpenseSqlEntity();
        expense.setUser(user);
        expense.setAmount(BigDecimal.valueOf(100.50));
        expense.setExpenseDate(LocalDateTime.now());
        expense.setDescription("Groceries");
        expense.setLocation("Berlin");

        ExpenseSqlEntity savedExpense = inMemoryExpenseCommandRepository.save(expense);

        Optional<ExpenseSqlEntity> result = inMemoryExpenseCommandRepository.findById(savedExpense.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(savedExpense.getId());
        assertThat(result.get().getUser()).isEqualTo(user);
        assertThat(result.get().getAmount()).isEqualTo(BigDecimal.valueOf(100.50));
        assertThat(result.get().getDescription()).isEqualTo("Groceries");
        assertThat(result.get().getLocation()).isEqualTo("Berlin");
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenExpenseDoesNotExist() {
        Optional<ExpenseSqlEntity> result = inMemoryExpenseCommandRepository.findById(999L);

        assertThat(result).isNotPresent();
    }

    @Test
    void deleteById_shouldRemoveExpenseSqlEntity_whenExpenseExists() {
        UserSqlEntity user = new UserSqlEntity();
        user.setId(1L);
        user.setEmail("testuser@example.com");

        ExpenseSqlEntity expense = new ExpenseSqlEntity();
        expense.setUser(user);
        expense.setAmount(BigDecimal.valueOf(100.50));
        expense.setExpenseDate(LocalDateTime.now());
        expense.setDescription("Groceries");
        expense.setLocation("Berlin");

        ExpenseSqlEntity savedExpense = inMemoryExpenseCommandRepository.save(expense);

        inMemoryExpenseCommandRepository.deleteById(savedExpense.getId());

        Optional<ExpenseSqlEntity> result = inMemoryExpenseCommandRepository.findById(savedExpense.getId());
        assertThat(result).isNotPresent();
    }

    @Test
    void deleteById_shouldDoNothing_whenExpenseDoesNotExist() {
        Long nonExistentId = 999L;

        inMemoryExpenseCommandRepository.deleteById(nonExistentId);

        Optional<ExpenseSqlEntity> result = inMemoryExpenseCommandRepository.findById(nonExistentId);
        assertThat(result).isNotPresent();
    }
}
