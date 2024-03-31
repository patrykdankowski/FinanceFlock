package com.patrykdankowski.financeflock.repository;

import com.patrykdankowski.financeflock.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsUserByEmail(String email);
    List<User> findAllByMainUserId(Long id);
}
