package com.patrykdankowski.financeflock.expense.model.entity;

import com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ExpenseDomainEntityTest {

    private ExpenseDomainEntity expense;

    @BeforeEach
    void setUp() {
        expense = ExpenseDomainEntity.buildExpense(1L, 2L, new BigDecimal("100.50"), LocalDateTime.now(), "Lunch", "Warsaw");
    }

    @Test
    void whenBuildExpenseWithAllParameters_thenAllFieldsAreInitializedCorrectly() {
        // Given
        Long id = 1L;
        Long userId = 2L;
        BigDecimal amount = new BigDecimal("100.50");
        LocalDateTime expenseDate = LocalDateTime.now();
        String description = "Lunch";
        String location = "Warsaw";

        // When
        ExpenseDomainEntity expense = ExpenseDomainEntity.buildExpense(id, userId, amount, expenseDate, description, location);

        // Then
        assertThat(expense.getId()).isEqualTo(id);
        assertThat(expense.getUserId()).isEqualTo(userId);
        assertThat(expense.getAmount()).isEqualByComparingTo(amount);
        assertThat(expense.getExpenseDate()).isNotNull();
        assertThat(expense.getDescription()).isEqualTo(description);
        assertThat(expense.getLocation()).isEqualTo(location);
    }

    @Test
    void whenBuildExpenseWithoutExpenseDate_thenExpenseDateIsSetToNow() {
        // Given
        Long id = 1L;
        Long userId = 2L;
        BigDecimal amount = new BigDecimal("100.50");
        String description = "Lunch";
        String location = "Warsaw";

        // When
        ExpenseDomainEntity expense = ExpenseDomainEntity.buildExpense(id, userId, amount, null, description, location);

        // Then
        assertThat(expense.getExpenseDate()).isNotNull();
    }

    @Test
    void givenExistingExpense_whenUpdateInfo_thenAmountIsUpdated() {
        // Given
        BigDecimal newAmount = new BigDecimal("200.75");

        // When
        expense.updateInfo(newAmount, null, null, null);

        // Then
        assertThat(expense.getAmount()).isEqualByComparingTo(newAmount);
    }

    @Test
    void givenExistingExpense_whenUpdateInfo_thenExpenseDateIsUpdated() {
        // Given
        LocalDateTime newExpenseDate = LocalDateTime.now().minusDays(2);

        // When
        expense.updateInfo(null, newExpenseDate, null, null);

        // Then
        assertThat(expense.getExpenseDate()).isEqualTo(newExpenseDate);
    }

    @Test
    void givenExistingExpense_whenUpdateInfo_thenDescriptionIsUpdated() {
        // Given
        String newDescription = "Dinner";

        // When
        expense.updateInfo(null, null, newDescription, null);

        // Then
        assertThat(expense.getDescription()).isEqualTo(newDescription);
    }

    @Test
    void givenExistingExpense_whenUpdateInfo_thenLocationIsUpdated() {
        // Given
        String newLocation = "Cracow";

        // When
        expense.updateInfo(null, null, null, newLocation);

        // Then
        assertThat(expense.getLocation()).isEqualTo(newLocation);
    }

    @Test
    void givenExistingExpense_whenUpdateInfoWithMultipleFields_thenFieldsAreUpdated() {
        // Given
        BigDecimal newAmount = new BigDecimal("300.00");
        LocalDateTime newExpenseDate = LocalDateTime.now().minusDays(1);
        String newDescription = "Groceries";
        String newLocation = "Gdansk";

        // When
        expense.updateInfo(newAmount, newExpenseDate, newDescription, newLocation);

        // Then
        assertThat(expense.getAmount()).isEqualByComparingTo(newAmount);
        assertThat(expense.getExpenseDate()).isEqualTo(newExpenseDate);
        assertThat(expense.getDescription()).isEqualTo(newDescription);
        assertThat(expense.getLocation()).isEqualTo(newLocation);
    }

    @Test
    void givenExistingExpense_whenUpdateInfoWithNullFields_thenFieldsRemainUnchanged() {
        // Given
        BigDecimal originalAmount = expense.getAmount();
        LocalDateTime originalDate = expense.getExpenseDate();
        String originalDescription = expense.getDescription();
        String originalLocation = expense.getLocation();

        // When
        expense.updateInfo(null, null, null, null);

        // Then
        assertThat(expense.getAmount()).isEqualByComparingTo(originalAmount);
        assertThat(expense.getExpenseDate()).isEqualTo(originalDate);
        assertThat(expense.getDescription()).isEqualTo(originalDescription);
        assertThat(expense.getLocation()).isEqualTo(originalLocation);
    }

    @Test
    void whenBuildExpenseWithNullAmount_thenAmountIsNotInitialized() {
        // Given
        Long id = 1L;
        Long userId = 2L;
        BigDecimal amount = null;
        LocalDateTime expenseDate = LocalDateTime.now();
        String description = "Lunch";
        String location = "Warsaw";

        // When
        ExpenseDomainEntity expense = ExpenseDomainEntity.buildExpense(id, userId, amount, expenseDate, description, location);

        // Then
        assertThat(expense.getAmount()).isNull();
    }

    @Test
    void whenBuildExpenseWithNegativeAmount_thenAmountIsInitialized() {
        // Given
        Long id = 1L;
        Long userId = 2L;
        BigDecimal negativeAmount = new BigDecimal("-100.50");
        LocalDateTime expenseDate = LocalDateTime.now();
        String description = "Lunch";
        String location = "Warsaw";

        // When
        ExpenseDomainEntity expense = ExpenseDomainEntity.buildExpense(id, userId, negativeAmount, expenseDate, description, location);

        // Then
        assertThat(expense.getAmount()).isEqualByComparingTo(negativeAmount);
    }

    @Test
    void whenBuildExpenseWithNullUserId_thenUserIdIsNull() {
        // Given
        Long id = 1L;
        Long userId = null;
        BigDecimal amount = new BigDecimal("100.50");
        LocalDateTime expenseDate = LocalDateTime.now();
        String description = "Lunch";
        String location = "Warsaw";

        // When
        ExpenseDomainEntity expense = ExpenseDomainEntity.buildExpense(id, userId, amount, expenseDate, description, location);

        // Then
        assertThat(expense.getUserId()).isNull();
    }

    @Test
    void whenBuildExpenseWithNullDescription_thenDescriptionIsNull() {
        // Given
        Long id = 1L;
        Long userId = 2L;
        BigDecimal amount = new BigDecimal("100.50");
        LocalDateTime expenseDate = LocalDateTime.now();
        String description = null;
        String location = "Warsaw";

        // When
        ExpenseDomainEntity expense = ExpenseDomainEntity.buildExpense(id, userId, amount, expenseDate, description, location);

        // Then
        assertThat(expense.getDescription()).isNull();
    }

    @Test
    void whenBuildExpenseWithNullLocation_thenLocationIsNull() {
        // Given
        Long id = 1L;
        Long userId = 2L;
        BigDecimal amount = new BigDecimal("100.50");
        LocalDateTime expenseDate = LocalDateTime.now();
        String description = "Lunch";
        String location = null;

        // When
        ExpenseDomainEntity expense = ExpenseDomainEntity.buildExpense(id, userId, amount, expenseDate, description, location);

        // Then
        assertThat(expense.getLocation()).isNull();
    }

    // 14. Test updateInfo z null amount
    @Test
    void givenExistingExpense_whenUpdateInfoWithNullAmount_thenAmountRemainsUnchanged() {
        // Given
        BigDecimal originalAmount = expense.getAmount();

        // When
        expense.updateInfo(null, null, null, null);

        // Then
        assertThat(expense.getAmount()).isEqualByComparingTo(originalAmount);
    }

    @Test
    void givenExistingExpense_whenUpdateInfoWithNegativeAmount_thenAmountIsUpdated() {
        // Given
        BigDecimal negativeAmount = new BigDecimal("-200.00");

        // When
        expense.updateInfo(negativeAmount, null, null, null);

        // Then
        assertThat(expense.getAmount()).isEqualByComparingTo(negativeAmount);
    }

    @Test
    void givenExistingExpense_whenUpdateInfoWithNullExpenseDate_thenExpenseDateRemainsUnchanged() {
        // Given
        LocalDateTime originalExpenseDate = expense.getExpenseDate();

        // When
        expense.updateInfo(null, null, null, null);

        // Then
        assertThat(expense.getExpenseDate()).isEqualTo(originalExpenseDate);
    }
}
