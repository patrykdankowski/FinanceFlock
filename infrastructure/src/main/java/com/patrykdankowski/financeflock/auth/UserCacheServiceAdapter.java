package com.patrykdankowski.financeflock.auth;

import com.patrykdankowski.financeflock.user.User;
import com.patrykdankowski.financeflock.user.UserCommandServicePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

@Service
@CacheConfig
@Slf4j
public class UserCacheServiceAdapter implements UserCacheServicePort {


    private final UserCommandServicePort userCommandService;

    public UserCacheServiceAdapter(final UserCommandServicePort userCommandService) {
        this.userCommandService = userCommandService;
    }


    //    @Cacheable(cacheNames = "userEmailCache", key = "#userEmail")
    @Override
    public User getUserFromEmail(String userEmail) {
        var user = userCommandService.findUserByEmail(userEmail);
        return user;
    }
}
