package com.patrykdankowski.financeflock.security.validation;

import com.patrykdankowski.financeflock.dto.RegisterDto;
import com.patrykdankowski.financeflock.exception.PasswordValidationException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class PasswordEqualsValidator implements ConstraintValidator<EqualsPassword, RegisterDto> {
    @Override
    public void initialize(EqualsPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(RegisterDto registerDto, ConstraintValidatorContext constraintValidatorContext) {
        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            throw new PasswordValidationException(List.of("Passwords must be equal"));
        }
        return true;


    }
}
