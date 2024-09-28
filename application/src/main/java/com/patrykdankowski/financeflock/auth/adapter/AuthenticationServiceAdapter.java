package com.patrykdankowski.financeflock.auth.adapter;

import com.patrykdankowski.financeflock.auth.port.AuthenticationServicePort;
import com.patrykdankowski.financeflock.auth.port.UserCacheServicePort;
import com.patrykdankowski.financeflock.auth.port.UserContextServicePort;
import com.patrykdankowski.financeflock.user.dto.SimpleUserDomainEntity;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
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
    public UserDomainEntity getFullUserFromContext() {


        Authentication authentication = userContextService.getAuthenticationFromContext();
        String userEmail = authentication.getName();
        return userCacheService.getUserFromEmail(userEmail);


    }

    @Override
    public SimpleUserDomainEntity getSimpleUserFromContext() {

        Authentication authentication = userContextService.getAuthenticationFromContext();
        String userEmail = authentication.getName();
        return userCacheService.getSimpleUserFromEmail(userEmail);
    }


}
