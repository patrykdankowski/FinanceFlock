package com.patrykdankowski.financeflock.exception;

import lombok.Getter;

@Getter
public class ResourceAlreadyExists extends RuntimeException {
    private String message;
    private String details;

    public ResourceAlreadyExists(final String message, final String details) {
        this.message = message;
        this.details = details;
    }
}
