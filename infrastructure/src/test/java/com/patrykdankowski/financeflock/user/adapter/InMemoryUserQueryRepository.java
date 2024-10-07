package com.patrykdankowski.financeflock.user.adapter;


import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryUserQueryRepository {

    private final Map<Long, UserSqlEntity> userStore = new HashMap<>();
    private long currentId = 1L;

    public UserSqlEntity save(UserSqlEntity userSqlEntity) {
        if (userSqlEntity.getId() == null) {
            userSqlEntity.setId(currentId++);
        }
        userStore.put(userSqlEntity.getId(), userSqlEntity);
        return userSqlEntity;
    }

    public List<UserSqlEntity> findAllByBudgetGroup_Id(Long groupId, int page, int size) {
        return userStore.values().stream()
                .filter(user -> user.getBudgetGroup() != null && user.getBudgetGroup().getId().equals(groupId))
                .skip(page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    public Optional<UserSqlEntity> findSimpleUserByEmail(String email) {
        return userStore.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    public Optional<UserSqlEntity> findUserDetailsByEmail(String email) {
        return userStore.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    public Optional<UserSqlEntity> findById(Long id) {
        return Optional.ofNullable(userStore.get(id));
    }

    public void clear() {
        userStore.clear();
        currentId = 1L;
    }
}
