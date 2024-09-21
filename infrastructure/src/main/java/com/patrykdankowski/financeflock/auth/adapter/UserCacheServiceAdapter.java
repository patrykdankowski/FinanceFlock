package com.patrykdankowski.financeflock.auth.adapter;

import com.patrykdankowski.financeflock.auth.port.UserCacheServicePort;
import com.patrykdankowski.financeflock.user.exception.UserNotFoundException;
import com.patrykdankowski.financeflock.user.dto.SimpleUserDomainEntity;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.port.UserCommandServicePort;
import com.patrykdankowski.financeflock.user.port.UserQueryRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig
@Slf4j
class UserCacheServiceAdapter implements UserCacheServicePort {


    private final UserCommandServicePort userCommandService;
    private final UserQueryRepositoryPort userQueryRepository;

    public UserCacheServiceAdapter(final UserCommandServicePort userCommandService, final UserQueryRepositoryPort userQueryRepository) {
        this.userCommandService = userCommandService;
        this.userQueryRepository = userQueryRepository;
    }


        @Cacheable(cacheNames = "userEmailCache", key = "#userEmail")
    @Override
    public UserDomainEntity getUserFromEmail(String userEmail) {
        var user = userCommandService.findUserByEmail(userEmail);
        return user;
    }

    @Override
    public SimpleUserDomainEntity getSimpleUserFromEmail(String userEmail) {
        return userQueryRepository.retrieveUserFromMail(userEmail).orElseThrow(() -> new UserNotFoundException(userEmail));

    }
}
