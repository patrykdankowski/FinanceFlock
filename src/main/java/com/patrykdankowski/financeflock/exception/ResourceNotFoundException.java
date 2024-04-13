package com.patrykdankowski.financeflock.exception;

import com.patrykdankowski.financeflock.constants.AppConstants;
import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private String message;
    private String details;

    public ResourceNotFoundException(String email, String details) {
        this.message = details;
        this.details = String.format(AppConstants.USER_NOT_FOUND, email);
    }

    public ResourceNotFoundException(Long id, String details) {
        this.message = details;
        this.details = String.format(AppConstants.USER_NOT_FOUND, id);
    }

}
