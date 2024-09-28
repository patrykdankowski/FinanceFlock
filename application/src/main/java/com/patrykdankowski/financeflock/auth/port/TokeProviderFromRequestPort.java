package com.patrykdankowski.financeflock.auth.port;

import jakarta.servlet.http.HttpServletRequest;

public interface TokeProviderFromRequestPort {
    String getTokenFromRequest(HttpServletRequest request);
}
