package com.patrykdankowski.financeflock.exception;

import lombok.Getter;

@Getter
public class ResourceNotBelongToUserException extends RuntimeException {
    private String name;
    private Long resourceId;

    public ResourceNotBelongToUserException(String name, Long resourceId) {
        this.name = name;
        this.resourceId = resourceId;
    }
}
