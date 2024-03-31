package com.patrykdankowski.financeflock.exception;

public class SubUserNotBelongToMainUserException extends RuntimeException{
    private Long id;

    public SubUserNotBelongToMainUserException(Long id) {
        this.id = id;
    }
}
