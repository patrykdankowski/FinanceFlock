package com.patrykdankowski.financeflock.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String email) {
        super(String.format("User with given email %s does not exist in out database", email));
    }

    public UserNotFoundException(Long id) {
        super(String.format("User with given id %d does not exist in out database", id));
    }
}
