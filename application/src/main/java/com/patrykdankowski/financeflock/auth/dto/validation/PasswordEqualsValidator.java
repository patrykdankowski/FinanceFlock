package com.patrykdankowski.financeflock.auth.dto.validation;

import com.patrykdankowski.financeflock.auth.dto.RegisterDtoRequest;
import com.patrykdankowski.financeflock.auth.exception.PasswordValidationException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class PasswordEqualsValidator implements ConstraintValidator<EqualsPassword, RegisterDtoRequest> {
    @Override
    public void initialize(EqualsPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(RegisterDtoRequest registerDtoRequest, ConstraintValidatorContext constraintValidatorContext) {
        if (!registerDtoRequest.getPassword().equals(registerDtoRequest.getConfirmPassword())) {
            throw new PasswordValidationException(List.of("Passwords must be equal"));
        }
        return true;


    }
}
