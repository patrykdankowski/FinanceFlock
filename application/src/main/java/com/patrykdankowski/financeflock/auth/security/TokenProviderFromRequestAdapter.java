package com.patrykdankowski.financeflock.auth.security;

import com.patrykdankowski.financeflock.auth.port.TokeProviderFromRequestPort;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TokenProviderFromRequestAdapter implements TokeProviderFromRequestPort {
    @Override
    public String getTokenFromRequest(final HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
