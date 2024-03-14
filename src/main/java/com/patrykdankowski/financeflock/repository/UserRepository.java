package com.patrykdankowski.financeflock.repository;

import com.patrykdankowski.financeflock.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
