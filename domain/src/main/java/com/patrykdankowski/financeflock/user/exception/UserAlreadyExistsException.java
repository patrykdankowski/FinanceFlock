package com.patrykdankowski.financeflock.user.exception;

import com.patrykdankowski.financeflock.common.AppConstants;
import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {
    private String email;
    private String details;

    public UserAlreadyExistsException(final String email) {
        super("User with email with email already exists");
        this.email = email;
        this.details = AppConstants.VALID_EMAIL_MESSAGE;
    }
}
