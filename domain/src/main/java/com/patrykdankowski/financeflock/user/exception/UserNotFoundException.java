package com.patrykdankowski.financeflock.user.exception;

import com.patrykdankowski.financeflock.common.AppConstants;
import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    private String details;

    public UserNotFoundException(String email) {
        super("User with email " + email + " not found");
        this.details = String.format(AppConstants.USER_NOT_FOUND, email);
    }

}
