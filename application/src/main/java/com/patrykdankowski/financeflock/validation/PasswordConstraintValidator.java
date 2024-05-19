package com.patrykdankowski.financeflock.validation;

import com.patrykdankowski.financeflock.auth.PasswordValidationException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String passwordToValid, ConstraintValidatorContext constraintValidatorContext) {
        if (passwordToValid == null) {
            return false;
        }

        boolean hasUpperCase = passwordToValid.matches(".*[A-Z].*");
        boolean hasSpecialChar = passwordToValid.matches(".*[!@#$%^&*()].*");
        boolean hasDigit = passwordToValid.matches(".*\\d.*");
        boolean isLongEnough = passwordToValid.length() >= 10;

        boolean isPasswordValid = hasUpperCase && hasSpecialChar && hasDigit && isLongEnough;

        List<String> errorMessages = new ArrayList<>();

        if (!isLongEnough) {
            errorMessages.add("Password must be at least 10 characters long. ");
        }
        if (!hasSpecialChar) {
            errorMessages.add("Password must contain at least 1 special character. ");
        }
        if (!hasDigit) {
            errorMessages.add("Password must contain at least 1 digit. ");
        }
        if (!hasUpperCase) {
            errorMessages.add("Password must contain at least 1 uppercase letter. ");
        }

        if (!errorMessages.isEmpty()) {
            throw new PasswordValidationException(errorMessages);
        }

        return isPasswordValid;
    }
}
