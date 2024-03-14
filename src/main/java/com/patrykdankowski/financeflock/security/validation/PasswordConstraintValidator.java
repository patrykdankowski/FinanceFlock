package com.patrykdankowski.financeflock.security.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

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

        StringBuilder errorMessages = new StringBuilder();

        if (!isLongEnough) {
            errorMessages.append("Password must be at least 10 characters long. ");
        }
        if (!hasSpecialChar) {
            errorMessages.append("Password must contain at least 1 special character. ");
        }
        if (!hasDigit) {
            errorMessages.append("Password must contain at least 1 digit. ");
        }
        if (!hasUpperCase) {
            errorMessages.append("Password must contain at least 1 uppercase letter. ");
        }

        if (!errorMessages.isEmpty()) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(errorMessages.toString()).addConstraintViolation();
        }

        return isPasswordValid;
    }
}
