package com.patrykdankowski.financeflock.expense.model.vo;


import com.patrykdankowski.financeflock.expense.exception.ExpenseValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AmountVOTest {

    @Test
    void givenValidAmount_whenCreateAmountVO_thenValueIsSetCorrectly() {
        // Given
        BigDecimal validAmount = new BigDecimal("100.50");

        // When
        AmountVO amountVO = new AmountVO(validAmount);

        // Then
        assertThat(amountVO.value()).isEqualByComparingTo(validAmount);
    }

    @Test
    void givenNullAmount_whenCreateAmountVO_thenThrowExpenseValidationException() {
        // Given
        BigDecimal nullAmount = null;

        // When & Then
        assertThatThrownBy(() -> new AmountVO(nullAmount))
                .isInstanceOf(ExpenseValidationException.class)
                .hasMessage("Expense amount must be greater than zero");
    }

    @Test
    void givenZeroAmount_whenCreateAmountVO_thenThrowExpenseValidationException() {
        // Given
        BigDecimal zeroAmount = BigDecimal.ZERO;

        // When & Then
        assertThatThrownBy(() -> new AmountVO(zeroAmount))
                .isInstanceOf(ExpenseValidationException.class)
                .hasMessage("Expense amount must be greater than zero");
    }

    @Test
    void givenNegativeAmount_whenCreateAmountVO_thenThrowExpenseValidationException() {
        // Given
        BigDecimal negativeAmount = new BigDecimal("-10.00");

        // When & Then
        assertThatThrownBy(() -> new AmountVO(negativeAmount))
                .isInstanceOf(ExpenseValidationException.class)
                .hasMessage("Expense amount must be greater than zero");
    }

    @Test
    void givenAmountWithMoreThanTwoDecimalPlaces_whenCreateAmountVO_thenThrowExpenseValidationException() {
        // Given
        BigDecimal invalidAmount = new BigDecimal("10.123");

        // When & Then
        assertThatThrownBy(() -> new AmountVO(invalidAmount))
                .isInstanceOf(ExpenseValidationException.class)
                .hasMessage("Amount cannot have more than two decimal places");
    }

    @Test
    void givenAmountWithMoreThanSevenDigitsBeforeDecimal_whenCreateAmountVO_thenThrowExpenseValidationException() {
        // Given
        BigDecimal invalidAmount = new BigDecimal("12345678.00");

        // When & Then
        assertThatThrownBy(() -> new AmountVO(invalidAmount))
                .isInstanceOf(ExpenseValidationException.class)
                .hasMessage("Amount cannot have more than seven digits before the decimal point");
    }

    @Test
    void givenAmountWithTwoDecimalPlaces_whenCreateAmountVO_thenValueIsSetCorrectly() {
        // Given
        BigDecimal validAmount = new BigDecimal("123.45");

        // When
        AmountVO amountVO = new AmountVO(validAmount);

        // Then
        assertThat(amountVO.value()).isEqualByComparingTo(validAmount);
    }

    @Test
    void givenAmountWithSevenDigitsBeforeDecimal_whenCreateAmountVO_thenValueIsSetCorrectly() {
        // Given
        BigDecimal validAmount = new BigDecimal("1234567.89");

        // When
        AmountVO amountVO = new AmountVO(validAmount);

        // Then
        assertThat(amountVO.value()).isEqualByComparingTo(validAmount);
    }
    @Test
    void givenAmountWithSevenDigitsAndTwoDecimalPlaces_whenCreateAmountVO_thenValueIsSetCorrectly() {
        // Given
        BigDecimal validAmount = new BigDecimal("1234567.89");

        // When
        AmountVO amountVO = new AmountVO(validAmount);

        // Then
        assertThat(amountVO.value()).isEqualByComparingTo(validAmount);
    }

    @Test
    void givenMinimalPositiveAmount_whenCreateAmountVO_thenValueIsSetCorrectly() {
        // Given
        BigDecimal minimalAmount = new BigDecimal("0.01");

        // When
        AmountVO amountVO = new AmountVO(minimalAmount);

        // Then
        assertThat(amountVO.value()).isEqualByComparingTo(minimalAmount);
    }

    @Test
    void givenValueJustAboveZero_whenCreateAmountVO_thenValueIsSetCorrectly() {
        // Given
        BigDecimal valueJustAboveZero = new BigDecimal("0.01");

        // When
        AmountVO amountVO = new AmountVO(valueJustAboveZero);

        // Then
        assertThat(amountVO.value()).isEqualByComparingTo(valueJustAboveZero);
    }

    @Test
    void givenVeryLargeValidAmount_whenCreateAmountVO_thenValueIsSetCorrectly() {
        // Given
        BigDecimal veryLargeAmount = new BigDecimal("9999999.99");

        // When
        AmountVO amountVO = new AmountVO(veryLargeAmount);

        // Then
        assertThat(amountVO.value()).isEqualByComparingTo(veryLargeAmount);
    }

    @Test
    void givenAmountWithNonStandardBigDecimalConstruction_whenCreateAmountVO_thenValueIsSetCorrectly() {
        // Given
        BigDecimal validAmount = BigDecimal.valueOf(100.50);

        // When
        AmountVO amountVO = new AmountVO(validAmount);

        // Then
        assertThat(amountVO.value()).isEqualByComparingTo(validAmount);
    }
}