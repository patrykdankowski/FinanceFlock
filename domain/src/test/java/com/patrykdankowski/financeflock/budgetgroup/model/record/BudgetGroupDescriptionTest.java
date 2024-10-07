package com.patrykdankowski.financeflock.budgetgroup.model.record;


import com.patrykdankowski.financeflock.budgetgroup.exception.BudgetGroupValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BudgetGroupDescriptionTest {

    @Test
    void givenValidDescription_whenCreateBudgetGroupDescription_thenNoExceptionThrown() {
        // given
        String validDescription = "Valid Description";

        // when
        BudgetGroupDescription budgetGroupDescription = new BudgetGroupDescription(validDescription);

        // then
        assertThat(budgetGroupDescription.getValue()).isEqualTo(validDescription);
    }

    @Test
    void givenNullDescription_whenCreateBudgetGroupDescription_thenThrowBudgetGroupValidationException() {
        // given
        String nullDescription = null;

        // when & then
        assertThatThrownBy(() -> new BudgetGroupDescription(nullDescription))
                .isInstanceOf(BudgetGroupValidationException.class)
                .hasMessage("Budget group description must be at least 5 characters length and cannot be longer than 25 characters");
    }

    @Test
    void givenEmptyDescription_whenCreateBudgetGroupDescription_thenThrowBudgetGroupValidationException() {
        // given
        String emptyDescription = "";

        // when & then
        assertThatThrownBy(() -> new BudgetGroupDescription(emptyDescription))
                .isInstanceOf(BudgetGroupValidationException.class)
                .hasMessage("Budget group description must be at least 5 characters length and cannot be longer than 25 characters");
    }

    @Test
    void givenShortDescription_whenCreateBudgetGroupDescription_thenThrowBudgetGroupValidationException() {
        // given
        String shortDescription = "1234";

        // when & then
        assertThatThrownBy(() -> new BudgetGroupDescription(shortDescription))
                .isInstanceOf(BudgetGroupValidationException.class)
                .hasMessage("Budget group description must be at least 5 characters length and cannot be longer than 25 characters");
    }

    @Test
    void givenLongDescription_whenCreateBudgetGroupDescription_thenThrowBudgetGroupValidationException() {
        // given
        String longDescription = "This description is way too long for a budget group.";

        // when & then
        assertThatThrownBy(() -> new BudgetGroupDescription(longDescription))
                .isInstanceOf(BudgetGroupValidationException.class)
                .hasMessage("Budget group description must be at least 5 characters length and cannot be longer than 25 characters");
    }

    @Test
    void givenExactlyFiveCharactersDescription_whenCreateBudgetGroupDescription_thenNoExceptionThrown() {
        // given
        String fiveCharDescription = "12345";

        // when
        BudgetGroupDescription budgetGroupDescription = new BudgetGroupDescription(fiveCharDescription);

        // then
        assertThat(budgetGroupDescription.getValue()).isEqualTo(fiveCharDescription);
    }

    @Test
    void givenExactlyTwentyFiveCharactersDescription_whenCreateBudgetGroupDescription_thenNoExceptionThrown() {
        // given
        String twentyFiveCharDescription = "1234567890123456789012345";

        // when
        BudgetGroupDescription budgetGroupDescription = new BudgetGroupDescription(twentyFiveCharDescription);

        // then
        assertThat(budgetGroupDescription.getValue()).isEqualTo(twentyFiveCharDescription);
    }
    @Test
    void givenValidDescription_whenCreateBudgetGroupDescription_thenInstanceIsCreated() {
        // given
        String validDescription = "Valid Budget Description";

        // when
        BudgetGroupDescription budgetGroupDescription = new BudgetGroupDescription(validDescription);

        // then
        assertThat(budgetGroupDescription.getValue()).isEqualTo(validDescription);
    }

    @Test
    void givenNullDescription_whenCreateBudgetGroupDescription_thenThrowsException() {
        // given
        String nullDescription = null;

        // when & then
        assertThatThrownBy(() -> new BudgetGroupDescription(nullDescription))
                .isInstanceOf(BudgetGroupValidationException.class)
                .hasMessage("Budget group description must be at least 5 characters length and cannot be longer than 25 characters");
    }

    @Test
    void givenEmptyDescription_whenCreateBudgetGroupDescription_thenThrowsException() {
        // given
        String emptyDescription = "";

        // when & then
        assertThatThrownBy(() -> new BudgetGroupDescription(emptyDescription))
                .isInstanceOf(BudgetGroupValidationException.class)
                .hasMessage("Budget group description must be at least 5 characters length and cannot be longer than 25 characters");
    }

    @Test
    void givenTooShortDescription_whenCreateBudgetGroupDescription_thenThrowsException() {
        // given
        String shortDescription = "abc";

        // when & then
        assertThatThrownBy(() -> new BudgetGroupDescription(shortDescription))
                .isInstanceOf(BudgetGroupValidationException.class)
                .hasMessage("Budget group description must be at least 5 characters length and cannot be longer than 25 characters");
    }

    @Test
    void givenTooLongDescription_whenCreateBudgetGroupDescription_thenThrowsException() {
        // given
        String longDescription = "This description is definitely too long for the budget group.";

        // when & then
        assertThatThrownBy(() -> new BudgetGroupDescription(longDescription))
                .isInstanceOf(BudgetGroupValidationException.class)
                .hasMessage("Budget group description must be at least 5 characters length and cannot be longer than 25 characters");
    }

    @Test
    void givenMinLengthDescription_whenCreateBudgetGroupDescription_thenInstanceIsCreated() {
        // given
        String minLengthDescription = "Valid";

        // when
        BudgetGroupDescription budgetGroupDescription = new BudgetGroupDescription(minLengthDescription);

        // then
        assertThat(budgetGroupDescription.getValue()).isEqualTo(minLengthDescription);
    }

    @Test
    void givenMaxLengthDescription_whenCreateBudgetGroupDescription_thenInstanceIsCreated() {
        // given
        String maxLengthDescription = "1234567890123456789012345";

        // when
        BudgetGroupDescription budgetGroupDescription = new BudgetGroupDescription(maxLengthDescription);

        // then
        assertThat(budgetGroupDescription.getValue()).isEqualTo(maxLengthDescription);
    }
}
