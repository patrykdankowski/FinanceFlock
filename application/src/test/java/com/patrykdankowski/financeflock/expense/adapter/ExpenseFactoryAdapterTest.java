package com.patrykdankowski.financeflock.expense.adapter;


import com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity;
import com.patrykdankowski.financeflock.expense.port.ExpenseFactoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ExpenseFactoryAdapterTest {

    private ExpenseFactoryPort expenseFactory;

    @BeforeEach
    void setUp() {
        expenseFactory = new ExpenseFactoryAdapter();
    }

    @Test
    void createExpanseFromRequest_shouldReturnExpenseDomainEntity_withCorrectValues() {
        // given
        Long id = 1L;
        Long userId = 100L;
        BigDecimal amount = new BigDecimal("99.99");
        LocalDateTime expenseDate = LocalDateTime.now();
        String description = "Test expense";
        String location = "Test location";

        // when
        ExpenseDomainEntity expense = expenseFactory.createExpanseFromRequest(id, userId, amount, expenseDate, description, location);

        // then
        assertThat(expense).isNotNull();
        assertThat(expense.getId()).isEqualTo(id);
        assertThat(expense.getUserId()).isEqualTo(userId);
        assertThat(expense.getAmount()).isEqualByComparingTo(amount);
        assertThat(expense.getExpenseDate()).isEqualTo(expenseDate);
        assertThat(expense.getDescription()).isEqualTo(description);
        assertThat(expense.getLocation()).isEqualTo(location);
    }

    @Test
    void createExpanseFromRequest_shouldHandleNullDescription() {
        // given
        Long id = 1L;
        Long userId = 100L;
        BigDecimal amount = new BigDecimal("50.00");
        LocalDateTime expenseDate = LocalDateTime.now();
        String description = null;
        String location = "Test location";

        // when
        ExpenseDomainEntity expense = expenseFactory.createExpanseFromRequest(id, userId, amount, expenseDate, description, location);

        // then
        assertThat(expense).isNotNull();
        assertThat(expense.getDescription()).isNull();
    }

    @Test
    void createExpanseFromRequest_shouldHandleEmptyLocation() {
        // given
        Long id = 2L;
        Long userId = 101L;
        BigDecimal amount = new BigDecimal("20.00");
        LocalDateTime expenseDate = LocalDateTime.now();
        String description = "Test with empty location";
        String location = "";

        // when
        ExpenseDomainEntity expense = expenseFactory.createExpanseFromRequest(id, userId, amount, expenseDate, description, location);

        // then
        assertThat(expense).isNotNull();
        assertThat(expense.getLocation()).isEmpty();
    }

    @Test
    void createExpanseFromRequest_shouldHandleZeroAmount() {
        // given
        Long id = 3L;
        Long userId = 102L;
        BigDecimal amount = BigDecimal.ZERO;
        LocalDateTime expenseDate = LocalDateTime.now();
        String description = "Zero amount expense";
        String location = "Test location";

        // when
        ExpenseDomainEntity expense = expenseFactory.createExpanseFromRequest(id, userId, amount, expenseDate, description, location);

        // then
        assertThat(expense).isNotNull();
        assertThat(expense.getAmount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void createExpanseFromRequest_shouldHandleNegativeAmount() {
        // given
        Long id = 4L;
        Long userId = 103L;
        BigDecimal amount = new BigDecimal("-10.00");
        LocalDateTime expenseDate = LocalDateTime.now();
        String description = "Negative amount";
        String location = "Test location";

        // when
        ExpenseDomainEntity expense = expenseFactory.createExpanseFromRequest(id, userId, amount, expenseDate, description, location);

        // then
        assertThat(expense).isNotNull();
        assertThat(expense.getAmount()).isEqualByComparingTo(amount);
    }


}