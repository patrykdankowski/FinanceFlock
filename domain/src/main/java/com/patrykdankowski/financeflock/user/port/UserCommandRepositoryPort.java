package com.patrykdankowski.financeflock.user.port;

import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserCommandRepositoryPort {
    Optional<UserDomainEntity> findByEmail(String email);

    UserDomainEntity save(UserDomainEntity user);

    List<UserDomainEntity> saveAll(List<UserDomainEntity> entities);

    List<UserDomainEntity> findAllByIdIn(List<Long> ids);

    boolean existsUserByEmail(String email);

    void updateLastLoginDate(LocalDateTime lastLoginDate, String email);

}
