package com.patrykdankowski.financeflock.cache;

import com.patrykdankowski.financeflock.user.User;
import com.patrykdankowski.financeflock.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig
@Slf4j
public class UserCacheService {


    private final UserService userService;

    public UserCacheService(final UserService userService) {
        this.userService = userService;
    }


//    @Cacheable(cacheNames = "userEmailCache", key = "#userEmail")
    public User getUserFromEmail(String userEmail) {
        var user = userService.findUserByEmail(userEmail);
        log.info("Cache " + user.getName());
        return user;
    }
}
