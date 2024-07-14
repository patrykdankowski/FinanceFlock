package com.patrykdankowski.financeflock.user.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {
    private String message;
    private String details;

    public UserAlreadyExistsException(final String message, final String details) {
        this.message = message;
        this.details = details;
    }
}
