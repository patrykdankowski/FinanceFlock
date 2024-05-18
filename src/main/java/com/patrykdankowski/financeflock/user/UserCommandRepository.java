package com.patrykdankowski.financeflock.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

 public interface UserCommandRepository extends Repository<User, Long> {
    //TODO uzywane w query i w command
    Optional<User> findByEmail(String email);

    User save(User user);

    List<User> saveAll(Iterable<User> entities);

    List<User> findAllById(Iterable<Long> ids);

    boolean existsUserByEmail(String email);

}
