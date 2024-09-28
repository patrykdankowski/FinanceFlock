package com.patrykdankowski.financeflock.auth.validation;

import com.patrykdankowski.financeflock.auth.dto.RegisterDto;
import com.patrykdankowski.financeflock.auth.exception.PasswordValidationException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordEqualsValidator implements ConstraintValidator<EqualsPassword, RegisterDto> {
    @Override
    public void initialize(EqualsPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(RegisterDto registerDto, ConstraintValidatorContext constraintValidatorContext) {
        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            throw new PasswordValidationException("Passwords must be equal");
        }
        return true;


    }
}
