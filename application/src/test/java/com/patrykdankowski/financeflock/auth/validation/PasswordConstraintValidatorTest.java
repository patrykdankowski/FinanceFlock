package com.patrykdankowski.financeflock.auth.validation;

import com.patrykdankowski.financeflock.auth.exception.PasswordValidationException;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PasswordConstraintValidatorTest {

    private PasswordConstraintValidator passwordConstraintValidator;

    @BeforeEach
    void setUp() {
        passwordConstraintValidator = new PasswordConstraintValidator();
    }

    @Test
    void whenPasswordIsTooShort_thenThrowPasswordValidationException() {
        String invalidPassword = "Short1!";
        assertThatThrownBy(() -> passwordConstraintValidator.isValid(invalidPassword, Mockito.mock(ConstraintValidatorContext.class)))
                .isInstanceOf(PasswordValidationException.class)
                .hasMessageContaining("10 characters long");
    }


    @Test
    void whenPasswordIsValid_thenReturnTrue() {
        String validPassword = "ValidPass1!";
        boolean result = passwordConstraintValidator.isValid(validPassword, Mockito.mock(ConstraintValidatorContext.class));
        assertThat(result).isTrue();
    }

    @Test
    void whenPasswordIsNull_thenReturnFalse() {
        String nullPassword = null;
        boolean result = passwordConstraintValidator.isValid(nullPassword, Mockito.mock(ConstraintValidatorContext.class));
        assertThat(result).isFalse();
    }

    @Test
    void whenPasswordIsEmpty_thenThrowPasswordValidationException() {
        String emptyPassword = "";
        assertThatThrownBy(() -> passwordConstraintValidator.isValid(emptyPassword, Mockito.mock(ConstraintValidatorContext.class)))
                .isInstanceOf(PasswordValidationException.class)
                .hasMessageContaining("10 characters long")
                .hasMessageContaining("1 special character")
                .hasMessageContaining("1 digit")
                .hasMessageContaining("1 uppercase letter");
    }

    @Test
    void whenPasswordContainsOnlyWhiteSpace_thenThrowPasswordValidationException() {
        String whitespacePassword = "          "; // 10 spacji
        assertThatThrownBy(() -> passwordConstraintValidator.isValid(whitespacePassword, Mockito.mock(ConstraintValidatorContext.class)))
                .isInstanceOf(PasswordValidationException.class)
                .hasMessageContaining("1 special character")
                .hasMessageContaining("1 digit")
                .hasMessageContaining("1 uppercase letter");
    }
}