package com.patrykdankowski.financeflock.user;

import org.springframework.data.repository.Repository;

public interface UserQueryRepositoryAdapter extends UserQueryRepositoryPort, Repository<UserDomainEntity,Long> {
}
