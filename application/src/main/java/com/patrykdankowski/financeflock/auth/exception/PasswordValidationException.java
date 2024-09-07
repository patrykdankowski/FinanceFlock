package com.patrykdankowski.financeflock.auth.exception;

public class PasswordValidationException extends RuntimeException {
    private final String errorMessage;

    public PasswordValidationException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
