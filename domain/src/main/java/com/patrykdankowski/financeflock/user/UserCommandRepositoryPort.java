package com.patrykdankowski.financeflock.user;

import java.util.List;
import java.util.Optional;

  interface UserCommandRepositoryPort {
    //TODO uzywane w query i w command
    Optional<UserDomainEntity> findByEmail(String email);

    Optional<UserDomainEntity> findById(Long id);

    UserDomainEntity save(UserDomainEntity user);

    List<UserDomainEntity> saveAll(List<UserDomainEntity> entities);

    List<UserDomainEntity> findAllByIdIn(List<Long> ids);

    boolean existsUserByEmail(String email);



}
