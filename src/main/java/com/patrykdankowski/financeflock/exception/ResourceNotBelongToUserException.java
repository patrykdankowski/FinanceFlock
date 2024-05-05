package com.patrykdankowski.financeflock.exception;

import lombok.Getter;

@Getter
public class ResourceNotBelongToUserException extends RuntimeException {
    private String name;
    private Long resourceId;
    private Long userId;

    public ResourceNotBelongToUserException(String name, Long resourceId) {
        this.name = name;
        this.resourceId = resourceId;
    }
    public ResourceNotBelongToUserException(Long userId, Long resourceId) {
        this.userId = userId;
        this.resourceId = resourceId;
    }

}
