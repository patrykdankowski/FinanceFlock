package com.patrykdankowski.financeflock.auth.port;

import org.springframework.security.core.Authentication;

public interface JwtTokenManagementPort {

    String generateJwtToken(Authentication authentication);

    boolean validateJwtToken(String token);

    String getUsernameFromJwtToken(String token);

    void deactivateToken(String token);
}
