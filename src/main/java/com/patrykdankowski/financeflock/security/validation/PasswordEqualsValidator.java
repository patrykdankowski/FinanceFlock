package com.patrykdankowski.financeflock.security.validation;

import com.patrykdankowski.financeflock.dto.RegisterDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordEqualsValidator implements ConstraintValidator<EqualsPassword, RegisterDto> {
    @Override
    public void initialize(EqualsPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(RegisterDto registerDto, ConstraintValidatorContext constraintValidatorContext) {
        return registerDto.getPassword().equals(registerDto.getConfirmPassword());
    }
}
