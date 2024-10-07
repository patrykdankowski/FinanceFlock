package com.patrykdankowski.financeflock.auth.adapter;

import com.patrykdankowski.financeflock.auth.entity.TokenSqlEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryTokenRepository {
    private final Map<Long, TokenSqlEntity> tokenStore = new HashMap<>();
    private long currentId = 1L;

    public void save(TokenSqlEntity token) {
        if (token.getId() == null) {
            token.setId(currentId++);
        }
        tokenStore.put(token.getId(), token);
    }

    public Optional<TokenSqlEntity> findByToken(String token) {
        return tokenStore.values().stream()
                .filter(t -> t.getToken().equals(token))
                .findFirst();
    }

    public Optional<TokenSqlEntity> findByUserEmail(String userEmail) {
        return tokenStore.values().stream()
                .filter(t -> t.getUserEmail().equals(userEmail))
                .findFirst();
    }

    public void clear() {
        tokenStore.clear();
        currentId = 1L;
    }
}