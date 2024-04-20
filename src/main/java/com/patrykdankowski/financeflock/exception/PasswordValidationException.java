package com.patrykdankowski.financeflock.exception;

import lombok.Getter;

import java.util.List;
 public class PasswordValidationException extends  RuntimeException{
    private final List<String> errorMessage;

    public PasswordValidationException(List<String> errorMessage) {
        super(errorMessage.toString());
        this.errorMessage = errorMessage;
    }

    List<String> getErrorMessage() {
        return errorMessage;
    }
}
