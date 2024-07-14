package com.patrykdankowski.financeflock.auth.port;

import org.springframework.security.core.Authentication;

public interface UserContextServicePort {
     Authentication getAuthenticationFromContext();
}
