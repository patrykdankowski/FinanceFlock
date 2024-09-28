package com.patrykdankowski.financeflock.auth.dto;

public record JwtAuthenticationResponse(String token, String tokenType) {

    public JwtAuthenticationResponse(String token) {
        this(token, "Bearer");
    }
}
