package com.patrykdankowski.financeflock.user;

import org.springframework.data.repository.Repository;

public interface UserQueryRepositoryAdapter extends UserQueryRepositoryPort, Repository<UserSqlEntity,Long> {
}
