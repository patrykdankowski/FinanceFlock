package com.patrykdankowski.financeflock.service;

import com.patrykdankowski.financeflock.entity.User;
import com.patrykdankowski.financeflock.exception.ResourceNotFoundException;
import com.patrykdankowski.financeflock.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.patrykdankowski.financeflock.constants.AppConstants.VALID_EMAIL_MESSAGE;

@Service
@RequiredArgsConstructor
@CacheConfig
@Slf4j
public class UserCacheService {
    private final UserRepository userRepository;


    @Cacheable(cacheNames = "userEmailCache", key = "#userEmail")
    public User getUserFromEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException(userEmail,VALID_EMAIL_MESSAGE));
//        log.info("Cache " +user.getName());
        return user;
    }
}
