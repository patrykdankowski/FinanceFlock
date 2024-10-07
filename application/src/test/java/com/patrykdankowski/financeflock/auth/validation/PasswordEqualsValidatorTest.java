package com.patrykdankowski.financeflock.auth.validation;


import com.patrykdankowski.financeflock.auth.dto.RegisterDto;
import com.patrykdankowski.financeflock.auth.exception.PasswordValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordEqualsValidatorTest {

    private final PasswordEqualsValidator validator = new PasswordEqualsValidator();

    @Test
    void whenPasswordsAreEqual_thenValidationPasses() {
        // given
        RegisterDto registerDto = new RegisterDto(
                "John Doe",
                "john.doe@example.com",
                "password123",
                "password123"
        );

        // when
        boolean isValid = validator.isValid(registerDto, null);

        // then
        assertTrue(isValid);
    }

    @Test
    void whenPasswordsAreNotEqual_thenThrowPasswordValidationException() {
        // given
        RegisterDto registerDto = new RegisterDto(
                "John Doe",
                "john.doe@example.com",
                "password123",
                "differentPassword"
        );

        // when / then
        assertThatThrownBy(() -> validator.isValid(registerDto, null))
                .isInstanceOf(PasswordValidationException.class)
                .hasMessageContaining("Passwords must be equal");
    }

    @Test
    void whenPasswordOrConfirmPasswordIsNull_thenThrowPasswordValidationException() {

        RegisterDto registerDtoWithNullPassword = new RegisterDto(
                "John Doe",
                "john.doe@example.com",
                null,
                "password123"
        );

        // when / then
        assertThatThrownBy(() -> validator.isValid(registerDtoWithNullPassword, null))
                .isInstanceOf(PasswordValidationException.class)
                .hasMessageContaining("Passwords must be provided and cannot be null");

        // given
        RegisterDto registerDtoWithNullConfirmPassword = new RegisterDto(
                "John Doe",
                "john.doe@example.com",
                "password123",
                null
        );

        // when / then
        assertThatThrownBy(() -> validator.isValid(registerDtoWithNullConfirmPassword, null))
                .isInstanceOf(PasswordValidationException.class)
                .hasMessageContaining("Passwords must be provided and cannot be null");
    }
}