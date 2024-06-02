package com.patrykdankowski.financeflock.mapper;

import com.patrykdankowski.financeflock.user.UserSqlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

interface UserMapperRepository extends JpaRepository<UserSqlEntity, Long> {

    @Query("SELECT u FROM UserSqlEntity u WHERE u.id IN :id")
    List<UserSqlEntity> findAllById(List<Long> id);

    Optional<UserSqlEntity> findById(Long id);

}
