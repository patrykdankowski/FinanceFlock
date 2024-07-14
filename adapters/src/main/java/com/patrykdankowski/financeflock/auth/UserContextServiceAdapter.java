package com.patrykdankowski.financeflock.auth;

import com.patrykdankowski.financeflock.auth.port.UserContextServicePort;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserContextServiceAdapter implements UserContextServicePort {

    @Override
    public Authentication getAuthenticationFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("No authenticated user found");
        }
        return authentication;
    }
}

