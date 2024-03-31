package com.patrykdankowski.financeflock.exception;

public class UserNotFoundException extends RuntimeException{
    private String email;
    private Long id;

    public UserNotFoundException(String email) {
        this.email = email;
    }
    public UserNotFoundException(Long id) {
        this.id = id;
    }
}
