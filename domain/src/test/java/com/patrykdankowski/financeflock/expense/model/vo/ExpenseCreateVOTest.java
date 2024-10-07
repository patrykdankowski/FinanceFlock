package com.patrykdankowski.financeflock.expense.model.vo;

import com.patrykdankowski.financeflock.expense.exception.ExpenseValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

public class ExpenseCreateVOTest {


    // 1. Test poprawnego utworzenia ExpenseCreateVO
    @Test
    void whenAllFieldsAreValid_thenExpenseCreateVOIsCreatedSuccessfully() {
        // given
        String description = "Valid Expense";
        AmountVO amountVO = new AmountVO(BigDecimal.valueOf(50.25));
        String location = "New York";
        LocalDateTime expenseDate = LocalDateTime.now();

        // when
        ExpenseCreateVO expenseCreateVO = new ExpenseCreateVO(description, amountVO, location, expenseDate);

        // then
        assertThat(expenseCreateVO.description()).isEqualTo(description);
        assertThat(expenseCreateVO.amountVO()).isEqualTo(amountVO);
        assertThat(expenseCreateVO.location()).isEqualTo(location);
        assertThat(expenseCreateVO.expenseDate()).isEqualTo(expenseDate);
    }

    // 2. Test dla opisu o długości poniżej 5 znaków
    @Test
    void givenDescriptionTooShort_whenCreateExpenseCreateVO_thenThrowExpenseValidationException() {
        // given
        String description = "abc";
        AmountVO amountVO = new AmountVO(BigDecimal.valueOf(50.25));
        String location = "New York";
        LocalDateTime expenseDate = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> new ExpenseCreateVO(description, amountVO, location, expenseDate))
                .isInstanceOf(ExpenseValidationException.class)
                .hasMessage("Expense description name must be at least 5 characters length and cannot be longer than 25 characters");
    }

    // 3. Test dla opisu o długości powyżej 25 znaków
    @Test
    void givenDescriptionTooLong_whenCreateExpenseCreateVO_thenThrowExpenseValidationException() {
        // given
        String description = "This description is way too long for an expense";
        AmountVO amountVO = new AmountVO(BigDecimal.valueOf(50.25));
        String location = "New York";
        LocalDateTime expenseDate = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> new ExpenseCreateVO(description, amountVO, location, expenseDate))
                .isInstanceOf(ExpenseValidationException.class)
                .hasMessage("Expense description name must be at least 5 characters length and cannot be longer than 25 characters");
    }

    // 4. Test dla opisu o poprawnej długości granicznej
    @Test
    void givenDescriptionOfBoundaryLength_whenCreateExpenseCreateVO_thenExpenseCreateVOIsCreatedSuccessfully() {
        // given
        String description = "Valid Description Length";
        AmountVO amountVO = new AmountVO(BigDecimal.valueOf(50.25));
        String location = "New York";
        LocalDateTime expenseDate = LocalDateTime.now();

        // when
        ExpenseCreateVO expenseCreateVO = new ExpenseCreateVO(description, amountVO, location, expenseDate);

        // then
        assertThat(expenseCreateVO.description()).isEqualTo(description);
        assertThat(expenseCreateVO.amountVO()).isEqualTo(amountVO);
        assertThat(expenseCreateVO.location()).isEqualTo(location);
        assertThat(expenseCreateVO.expenseDate()).isEqualTo(expenseDate);
    }

    @Test
    void whenExpenseDateIsNull_thenSetToNow() {
        // given
        String description = "Valid description";
        AmountVO amountVO = new AmountVO(BigDecimal.valueOf(100));
        String location = "Some location";

        // when
        ExpenseCreateVO expenseCreateVO = new ExpenseCreateVO(description, amountVO, location, null);

        // then
        LocalDateTime now = LocalDateTime.now();
        assertThat(SECONDS.between(expenseCreateVO.expenseDate(), now))
                .isLessThanOrEqualTo(1);
    }
    // 6. Test dla opisu dokładnie 5-znakowego
    @Test
    void givenDescriptionOfFiveCharacters_whenCreateExpenseCreateVO_thenExpenseCreateVOIsCreatedSuccessfully() {
        // given
        String description = "ABCDE";
        AmountVO amountVO = new AmountVO(BigDecimal.valueOf(10));
        String location = "London";
        LocalDateTime expenseDate = LocalDateTime.now();

        // when
        ExpenseCreateVO expenseCreateVO = new ExpenseCreateVO(description, amountVO, location, expenseDate);

        // then
        assertThat(expenseCreateVO.description()).isEqualTo(description);
        assertThat(expenseCreateVO.amountVO()).isEqualTo(amountVO);
        assertThat(expenseCreateVO.location()).isEqualTo(location);
        assertThat(expenseCreateVO.expenseDate()).isEqualTo(expenseDate);
    }

}
