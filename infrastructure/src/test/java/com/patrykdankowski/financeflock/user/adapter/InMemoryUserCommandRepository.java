package com.patrykdankowski.financeflock.user.adapter;

import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryUserCommandRepository {

    private final Map<Long, UserSqlEntity> userStore = new HashMap<>();
    private long currentId = 1L;

    public UserSqlEntity save(UserSqlEntity userSqlEntity) {
        if (userSqlEntity.getId() == null) {
            userSqlEntity.setId(currentId++);
        }
        userStore.put(userSqlEntity.getId(), userSqlEntity);
        return userSqlEntity;
    }

    public Optional<UserSqlEntity> findByEmail(String email) {
        return userStore.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    public List<UserSqlEntity> findAllByIdIn(List<Long> ids) {
        return userStore.values().stream()
                .filter(user -> ids.contains(user.getId()))
                .collect(Collectors.toList());
    }

    public boolean existsUserByEmail(String email) {
        return userStore.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    public void updateLastLoggedInAt(LocalDateTime lastLoggedInAt, String email) {
        userStore.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .ifPresent(user -> user.setLastLoggedInAt(lastLoggedInAt));
    }

    public Optional<UserSqlEntity> findById(Long id) {
        return Optional.ofNullable(userStore.get(id));
    }
}
