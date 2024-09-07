package com.patrykdankowski.financeflock.user.port;

import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;

import java.util.List;
import java.util.Optional;

public interface UserCommandRepositoryPort {
    //TODO uzywane w query i w command
    Optional<UserDomainEntity> findByEmail(String email);

    Optional<UserDomainEntity> findById(Long id);

    UserDomainEntity save(UserDomainEntity user);

    List<UserDomainEntity> saveAll(List<UserDomainEntity> entities);

    List<UserDomainEntity> findAllByIdIn(List<Long> ids);

    boolean existsUserByEmail(String email);

}
