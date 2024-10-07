package com.patrykdankowski.financeflock.expense.adapter;

import com.patrykdankowski.financeflock.expense.dto.ExpenseDto;
import com.patrykdankowski.financeflock.expense.entity.ExpenseSqlEntity;
import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryExpenseQueryRepositoryTest {

    private InMemoryExpenseQueryRepository inMemoryExpenseQueryRepository;

    @BeforeEach
    void setUp() {
        inMemoryExpenseQueryRepository = new InMemoryExpenseQueryRepository();
    }


    @Test
    void save_shouldPersistExpenseSqlEntity_whenEntityIsNew() {
        ExpenseSqlEntity expense = createExpense(null, "Groceries", BigDecimal.valueOf(50.00), "Berlin");

        ExpenseSqlEntity savedExpense = inMemoryExpenseQueryRepository.save(expense);

        assertThat(savedExpense.getId()).isNotNull();
        assertThat(savedExpense.getDescription()).isEqualTo("Groceries");
        assertThat(savedExpense.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(50.00));
        assertThat(savedExpense.getLocation()).isEqualTo("Berlin");
    }

    @Test
    void save_shouldUpdateExistingExpenseSqlEntity_whenEntityHasId() {
        ExpenseSqlEntity expense = createExpense(null, "Groceries", BigDecimal.valueOf(50.00), "Berlin");
        ExpenseSqlEntity savedExpense = inMemoryExpenseQueryRepository.save(expense);

        savedExpense.setDescription("Updated Groceries");
        savedExpense.setAmount(BigDecimal.valueOf(100.00));

        ExpenseSqlEntity updatedExpense = inMemoryExpenseQueryRepository.save(savedExpense);

        assertThat(updatedExpense.getId()).isEqualTo(savedExpense.getId());
        assertThat(updatedExpense.getDescription()).isEqualTo("Updated Groceries");
        assertThat(updatedExpense.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(100.00));
    }


    @Test
    void findExpensesForUser_shouldReturnExpenses_whenUserHasExpenses() {
        UserSqlEntity user = createUser(1L, "user1@example.com");

        ExpenseSqlEntity expense1 = createExpense(null, "Groceries", BigDecimal.valueOf(50.00), "Berlin", user);
        ExpenseSqlEntity expense2 = createExpense(null, "Fuel", BigDecimal.valueOf(30.00), "Katowice", user);

        inMemoryExpenseQueryRepository.save(expense1);
        inMemoryExpenseQueryRepository.save(expense2);

        List<ExpenseDto> expenses = inMemoryExpenseQueryRepository.findExpensesForUser(user.getId());

        assertThat(expenses).hasSize(2);
        assertThat(expenses).extracting(ExpenseDto::getDescription).containsExactlyInAnyOrder("Groceries", "Fuel");
        assertThat(expenses).extracting(ExpenseDto::getAmount).containsExactlyInAnyOrder(BigDecimal.valueOf(50.00), BigDecimal.valueOf(30.00));
    }

    @Test
    void findExpensesForUser_shouldReturnEmptyList_whenUserHasNoExpenses() {
        UserSqlEntity user = createUser(2L, "user2@example.com");

        List<ExpenseDto> expenses = inMemoryExpenseQueryRepository.findExpensesForUser(user.getId());

        assertThat(expenses).isEmpty();
    }

    @Test
    void findExpensesForUser_shouldReturnEmptyList_whenNoExpensesExistInRepository() {
        UserSqlEntity user = createUser(1L, "user1@example.com");

        List<ExpenseDto> expenses = inMemoryExpenseQueryRepository.findExpensesForUser(user.getId());

        assertThat(expenses).isEmpty();
    }

    @Test
    void findExpensesForUser_shouldReturnOnlyExpensesForSpecificUser() {
        UserSqlEntity user1 = createUser(1L, "user1@example.com");
        UserSqlEntity user2 = createUser(2L, "user2@example.com");

        ExpenseSqlEntity expense1 = createExpense(null, "Groceries", BigDecimal.valueOf(50.00), "Berlin", user1);
        ExpenseSqlEntity expense2 = createExpense(null, "Fuel", BigDecimal.valueOf(30.00), "Katowice", user2);

        inMemoryExpenseQueryRepository.save(expense1);
        inMemoryExpenseQueryRepository.save(expense2);

        List<ExpenseDto> user1Expenses = inMemoryExpenseQueryRepository.findExpensesForUser(user1.getId());
        List<ExpenseDto> user2Expenses = inMemoryExpenseQueryRepository.findExpensesForUser(user2.getId());

        assertThat(user1Expenses).hasSize(1);
        assertThat(user1Expenses.get(0).getDescription()).isEqualTo("Groceries");

        assertThat(user2Expenses).hasSize(1);
        assertThat(user2Expenses.get(0).getDescription()).isEqualTo("Fuel");
    }


    private ExpenseSqlEntity createExpense(Long id, String description, BigDecimal amount, String location) {
        return createExpense(id, description, amount, location, createUser(1L, "user@example.com"));
    }

    private ExpenseSqlEntity createExpense(Long id, String description, BigDecimal amount, String location, UserSqlEntity user) {
        ExpenseSqlEntity expense = new ExpenseSqlEntity();
        expense.setId(id);
        expense.setDescription(description);
        expense.setAmount(amount);
        expense.setExpenseDate(LocalDateTime.now());
        expense.setLocation(location);
        expense.setUser(user);
        return expense;
    }

    private UserSqlEntity createUser(Long id, String email) {
        UserSqlEntity user = new UserSqlEntity();
        user.setId(id);
        user.setEmail(email);
        return user;
    }
}
