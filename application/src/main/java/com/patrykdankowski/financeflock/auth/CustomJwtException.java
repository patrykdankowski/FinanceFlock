package com.patrykdankowski.financeflock.auth;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomJwtException extends RuntimeException {
    private HttpStatus httpStatus;
    private String message;

    public CustomJwtException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
