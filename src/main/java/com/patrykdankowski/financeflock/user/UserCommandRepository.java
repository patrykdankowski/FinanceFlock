package com.patrykdankowski.financeflock.user;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

interface UserCommandRepository extends Repository<User, Long> {
    Optional<User> findByEmail(String email);

    User save(User user);

    List<User> saveAll(Iterable<User> entities);
}
