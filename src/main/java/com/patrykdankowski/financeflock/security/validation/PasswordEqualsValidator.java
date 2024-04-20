package com.patrykdankowski.financeflock.security.validation;

import com.patrykdankowski.financeflock.user.RegisterDtoRequest;
import com.patrykdankowski.financeflock.exception.PasswordValidationException;
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
