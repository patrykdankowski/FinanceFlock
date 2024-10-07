package com.patrykdankowski.financeflock.auth.port;

import jakarta.servlet.http.HttpServletRequest;

public interface TokenProviderFromRequestPort {
    String getTokenFromRequest(HttpServletRequest request);
}
