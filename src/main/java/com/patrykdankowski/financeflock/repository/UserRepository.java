package com.patrykdankowski.financeflock.repository;

import com.patrykdankowski.financeflock.dto.projections.UserDtoProjections;
import com.patrykdankowski.financeflock.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsUserByEmail(String email);

//    @Query("SELECT u.name  FROM users u JOIN FETCH u.expenseList e WHERE u.shareData = true")
//@Query("SELECT u.name as name FROM users u JOIN u.expenseList e WHERE u.shareData = true")
@Query("SELECT u.name FROM users u LEFT JOIN  u.expenseList e WHERE u.shareData = true")

    Set<UserDtoProjections> findAllByShareDataIsTrue();



}
