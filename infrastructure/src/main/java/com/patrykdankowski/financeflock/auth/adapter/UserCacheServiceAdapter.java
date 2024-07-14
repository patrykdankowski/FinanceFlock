package com.patrykdankowski.financeflock.auth.adapter;

import com.patrykdankowski.financeflock.auth.port.UserCacheServicePort;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.port.UserCommandServicePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

@Service
@CacheConfig
@Slf4j
 class UserCacheServiceAdapter implements UserCacheServicePort {


    private final UserCommandServicePort userCommandService;

    public UserCacheServiceAdapter(final UserCommandServicePort userCommandService) {
        this.userCommandService = userCommandService;
    }


    //    @Cacheable(cacheNames = "userEmailCache", key = "#userEmail")
    @Override
    public UserDomainEntity getUserFromEmail(String userEmail) {
        var user = userCommandService.findUserByEmail(userEmail);
        return user;
    }
}
