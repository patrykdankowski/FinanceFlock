package com.patrykdankowski.financeflock.expense.adapter;

import com.patrykdankowski.financeflock.expense.exception.ExpenseValidationException;
import com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ExpenseValidatorAdapterTest {

    private ExpenseValidatorAdapter expenseValidatorAdapter;

    @BeforeEach
    void setUp() {
        expenseValidatorAdapter = new ExpenseValidatorAdapter();
    }

    @Test
    void validateAccessToExpense_shouldPass_whenUserHasAccess() {
        // given
        Long userId = 1L;
        Long expenseId = 101L;

        Set<Long> expenseList = new HashSet<>();
        expenseList.add(expenseId);

        UserDomainEntity user = UserDomainEntity.buildUser(userId, "User1", "password", "user1@example.com", LocalDateTime.now());
        user.getExpenseListId().addAll(expenseList);

        ExpenseDomainEntity expense = ExpenseDomainEntity.buildExpense(expenseId, userId, BigDecimal.valueOf(100.0), LocalDateTime.now(), "Lunch", "Restaurant");

        // when & then
        assertDoesNotThrow(() -> expenseValidatorAdapter.validateAccessToExpense(user, expense));
    }

    @Test
    void validateAccessToExpense_shouldThrowException_whenUserDoesNotOwnExpense() {
        // given
        Long userId = 1L;
        Long expenseId = 101L;
        Long differentUserId = 2L;

        Set<Long> expenseList = new HashSet<>();
        expenseList.add(expenseId);

        UserDomainEntity user = UserDomainEntity.buildUser(userId, "User1", "password", "user1@example.com", LocalDateTime.now());
        user.getExpenseListId().addAll(expenseList);

        ExpenseDomainEntity expense = ExpenseDomainEntity.buildExpense(expenseId, differentUserId, BigDecimal.valueOf(100.0), LocalDateTime.now(), "Lunch", "Restaurant");

        // when & then
        assertThatThrownBy(() -> expenseValidatorAdapter.validateAccessToExpense(user, expense))
                .isInstanceOf(ExpenseValidationException.class)
                .hasMessageContaining("Cannot access expense");
    }

    @Test
    void validateAccessToExpense_shouldThrowException_whenExpenseNotInUserList() {
        // given
        Long userId = 1L;
        Long expenseId = 101L;


        UserDomainEntity user = UserDomainEntity.buildUser(userId, "User1", "password", "user1@example.com", LocalDateTime.now());

        ExpenseDomainEntity expense = ExpenseDomainEntity.buildExpense(expenseId, userId, BigDecimal.valueOf(100.0), LocalDateTime.now(), "Lunch", "Restaurant");

        // when & then
        assertThatThrownBy(() -> expenseValidatorAdapter.validateAccessToExpense(user, expense))
                .isInstanceOf(ExpenseValidationException.class)
                .hasMessageContaining("Cannot access expense");
    }

    @Test
    void validateAccessToExpense_shouldThrowException_whenUserAndExpenseIdMismatch() {
        // given
        Long userId = 1L;
        Long expenseId = 101L;

        Set<Long> expenseList = new HashSet<>();
        expenseList.add(expenseId);

        UserDomainEntity user = UserDomainEntity.buildUser(userId, "User1", "password", "user1@example.com", LocalDateTime.now());
        user.getExpenseListId().addAll(expenseList);

        ExpenseDomainEntity expense = ExpenseDomainEntity.buildExpense(expenseId, 999L, BigDecimal.valueOf(100.0), LocalDateTime.now(), "Dinner", "Restaurant");

        // when & then
        assertThatThrownBy(() -> expenseValidatorAdapter.validateAccessToExpense(user, expense))
                .isInstanceOf(ExpenseValidationException.class)
                .hasMessageContaining("Cannot access expense");
    }





    @Test
    void validateAccessToExpense_shouldThrowException_whenUserHasEmptyExpenseList() {
        // given
        Long userId = 1L;
        Long expenseId = 101L;

        UserDomainEntity user = UserDomainEntity.buildUser(userId, "User1", "password", "user1@example.com", LocalDateTime.now());

        ExpenseDomainEntity expense = ExpenseDomainEntity.buildExpense(expenseId, userId, BigDecimal.valueOf(100.0), LocalDateTime.now(), "Lunch", "Restaurant");

        // when & then
        assertThatThrownBy(() -> expenseValidatorAdapter.validateAccessToExpense(user, expense))
                .isInstanceOf(ExpenseValidationException.class)
                .hasMessageContaining("Cannot access expense");
    }

    @Test
    void validateAccessToExpense_shouldThrowException_whenExpenseIdIsNotInUserList() {
        // given
        Long userId = 1L;
        Long expenseId = 101L;

        Set<Long> expenseList = new HashSet<>();
        expenseList.add(102L);

        UserDomainEntity user = UserDomainEntity.buildUser(userId, "User1", "password", "user1@example.com", LocalDateTime.now());
        user.getExpenseListId().addAll(expenseList);

        ExpenseDomainEntity expense = ExpenseDomainEntity.buildExpense(expenseId, userId, BigDecimal.valueOf(100.0), LocalDateTime.now(), "Dinner", "Restaurant");

        // when & then
        assertThatThrownBy(() -> expenseValidatorAdapter.validateAccessToExpense(user, expense))
                .isInstanceOf(ExpenseValidationException.class)
                .hasMessageContaining("Cannot access expense");
    }

    @Test
    void validateAccessToExpense_shouldThrowException_whenUserAndExpenseHaveDifferentIds() {
        // given
        Long userId = 1L;
        Long expenseId = 101L;

        Set<Long> expenseList = new HashSet<>();
        expenseList.add(expenseId);

        UserDomainEntity user = UserDomainEntity.buildUser(userId, "User1", "password", "user1@example.com", LocalDateTime.now());
        user.getExpenseListId().addAll(expenseList);

        ExpenseDomainEntity expense = ExpenseDomainEntity.buildExpense(expenseId, 2L, BigDecimal.valueOf(100.0), LocalDateTime.now(), "Travel", "Hotel");

        // when & then
        assertThatThrownBy(() -> expenseValidatorAdapter.validateAccessToExpense(user, expense))
                .isInstanceOf(ExpenseValidationException.class)
                .hasMessageContaining("Cannot access expense");
    }

    @Test
    void validateAccessToExpense_shouldPass_whenUserOwnsExpenseAndIsInList() {
        // given
        Long userId = 1L;
        Long expenseId = 101L;

        Set<Long> expenseList = new HashSet<>();
        expenseList.add(expenseId);

        UserDomainEntity user = UserDomainEntity.buildUser(userId, "User1", "password", "user1@example.com", LocalDateTime.now());
        user.getExpenseListId().addAll(expenseList);

        ExpenseDomainEntity expense = ExpenseDomainEntity.buildExpense(expenseId, userId, BigDecimal.valueOf(150.0), LocalDateTime.now(), "Groceries", "Supermarket");

        // when & then
        assertDoesNotThrow(() -> expenseValidatorAdapter.validateAccessToExpense(user, expense));
    }
}