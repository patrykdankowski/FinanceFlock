package com.patrykdankowski.financeflock.auth;

import com.patrykdankowski.financeflock.cache.UserCacheService;
import com.patrykdankowski.financeflock.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
class AuthenticationServiceImpl implements AuthenticationService {
    private final UserContextService userContextService;
    private final UserCacheService userCacheService;

    AuthenticationServiceImpl(final UserContextService userContextService, final UserCacheService userCacheService) {
        this.userContextService = userContextService;
        this.userCacheService = userCacheService;
    }


    @Override
    public User getUserFromContext() {


        Authentication authentication = userContextService.getAuthenticationFromContext();
        String userEmail = authentication.getName();
        return userCacheService.getUserFromEmail(userEmail);
    }


}
