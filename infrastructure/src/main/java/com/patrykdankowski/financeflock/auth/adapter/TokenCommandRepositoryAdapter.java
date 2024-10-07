package com.patrykdankowski.financeflock.auth.adapter;

import com.patrykdankowski.financeflock.auth.entity.TokenSqlEntity;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface TokenCommandRepositoryAdapter extends Repository<TokenSqlEntity, Long> {
    Optional<TokenSqlEntity> findByToken(String token);

    void save(TokenSqlEntity tokenSqlEntity);

    Optional<TokenSqlEntity> findByUserEmail(String userEmail);
}
