package com.patrykdankowski.financeflock.auth;

import com.patrykdankowski.financeflock.user.UserDomainEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
class AuthenticationServiceAdapter implements AuthenticationServicePort {
    private final UserContextServicePort userContextService;
    private final UserCacheServicePort userCacheService;

    AuthenticationServiceAdapter(final UserContextServiceAdapter userContextServiceAdapter,
                                 final UserCacheServicePort userCacheService) {
        this.userContextService = userContextServiceAdapter;
        this.userCacheService = userCacheService;
    }


    @Override
    public UserDomainEntity getUserFromContext() {

        Authentication authentication = userContextService.getAuthenticationFromContext();
        String userEmail = authentication.getName();
        return userCacheService.getUserFromEmail(userEmail);
    }


}
