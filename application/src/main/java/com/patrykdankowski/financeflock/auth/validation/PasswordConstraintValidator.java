package com.patrykdankowski.financeflock.auth.validation;

import com.patrykdankowski.financeflock.auth.exception.PasswordValidationException;
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
//        List<String> errorMessages = new ArrayList<>();

        if (!isLongEnough) {
            errorMessages.add("10 characters long");
        }
        if (!hasSpecialChar) {

            errorMessages.add("1 special character");
        }
        if (!hasDigit) {

            errorMessages.add("1 digit");
        }
        if (!hasUpperCase) {

            errorMessages.add("1 uppercase letter");
        }

        if (!errorMessages.isEmpty()) {

            StringBuilder combinedErrorMessages = new StringBuilder();
            combinedErrorMessages.append("Password must contain at least ");
            for (int i = 0; i < errorMessages.size(); i++) {

                combinedErrorMessages.append(errorMessages.get(i));
                if (i < errorMessages.size() - 1) {
                    combinedErrorMessages.append(", ");

                }
            }
            throw new PasswordValidationException(combinedErrorMessages.toString());
        }
        return true;
    }
}
