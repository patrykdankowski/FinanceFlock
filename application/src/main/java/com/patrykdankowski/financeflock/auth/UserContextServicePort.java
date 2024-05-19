package com.patrykdankowski.financeflock.auth;

import org.springframework.security.core.Authentication;

 interface UserContextServicePort {
     Authentication getAuthenticationFromContext();
}
