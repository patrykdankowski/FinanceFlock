package com.patrykdankowski.financeflock.auth;

import com.patrykdankowski.financeflock.auth.dto.CustomUserDetails;
import com.patrykdankowski.financeflock.auth.port.AuthenticationServicePort;
import com.patrykdankowski.financeflock.auth.port.UserCacheServicePort;
import com.patrykdankowski.financeflock.auth.port.UserContextServicePort;
import com.patrykdankowski.financeflock.user.dto.SimpleUserDomainEntity;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

//        Authentication authentication = userContextService.getAuthenticationFromContext();
//        String userEmail = authentication.getName();
//        return userCacheService.getUserFromEmail(userEmail);
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getUserDomainEntity(); // Zwraca pełny obiekt UserDomainEntity
        }
        throw new RuntimeException("User not found in security context");

    }

    @Override
    public SimpleUserDomainEntity getSimpleUserFromContext() {

        Authentication authentication = userContextService.getAuthenticationFromContext();
        String userEmail = authentication.getName();
        return userCacheService.getSimpleUserFromEmail(userEmail);
    }


}
