package com.patrykdankowski.financeflock.user.exception;

import com.patrykdankowski.financeflock.common.AppConstants;
import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {
    private String message;
    private String details;

    public UserAlreadyExistsException(final String message) {
        this.message = message;
        this.details = AppConstants.VALID_EMAIL_MESSAGE;
    }
}
