package com.patrykdankowski.financeflock.exception;

import lombok.Getter;

@Getter
public class ResourceNotBelongToUserException extends RuntimeException {
    private String userEmail;
    private Long resourceId;

    public ResourceNotBelongToUserException(String userEmail, Long resourceId) {
        this.userEmail = userEmail;
        this.resourceId = resourceId;
    }
}
