package com.patrykdankowski.financeflock.auth.port;

import org.springframework.security.core.Authentication;

public interface JwtTokenProviderPort {

    String generateJwtToken(Authentication authentication);

    boolean validateJwtToken(String token);

    String getUsernameFromJwtToken(String token);
}
