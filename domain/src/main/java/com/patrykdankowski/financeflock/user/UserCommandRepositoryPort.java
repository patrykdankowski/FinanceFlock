package com.patrykdankowski.financeflock.user;

import java.util.List;
import java.util.Optional;

 public interface UserCommandRepositoryPort {
    //TODO uzywane w query i w command
    Optional<UserDomainEntity> findByEmail(String email);

    UserDomainEntity save(UserDomainEntity user);

    List<UserDomainEntity> saveAll(Iterable<UserDomainEntity> entities);

    List<UserDomainEntity> findAllById(Iterable<Long> ids);

    boolean existsUserByEmail(String email);

}
