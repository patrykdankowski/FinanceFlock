package com.patrykdankowski.financeflock.cache;

import com.patrykdankowski.financeflock.user.User;
import com.patrykdankowski.financeflock.user.UserCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

@Service
@CacheConfig
@Slf4j
public class UserCacheService {


    private final UserCommandService userCommandService;

    public UserCacheService(final UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }


//    @Cacheable(cacheNames = "userEmailCache", key = "#userEmail")
    public User getUserFromEmail(String userEmail) {
        var user = userCommandService.findUserByEmail(userEmail);
        log.info("Cache " + user.getName());
        return user;
    }
}
