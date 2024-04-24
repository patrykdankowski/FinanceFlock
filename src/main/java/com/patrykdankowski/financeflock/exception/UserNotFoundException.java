package com.patrykdankowski.financeflock.exception;

import com.patrykdankowski.financeflock.constants.AppConstants;
import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    private String details;

    public UserNotFoundException(String email) {

        this.details = String.format(AppConstants.USER_NOT_FOUND, email);
    }

    public UserNotFoundException(Long userId) {

        this.details = String.format(AppConstants.USER_NOT_FOUND, userId);
    }

}
