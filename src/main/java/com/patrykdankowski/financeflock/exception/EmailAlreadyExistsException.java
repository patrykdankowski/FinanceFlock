package com.patrykdankowski.financeflock.exception;

import lombok.Getter;

@Getter
public class EmailAlreadyExistsException extends RuntimeException{

private String email;

    public EmailAlreadyExistsException(String email) {
        super(email);
        this.email = email;
    }
}
