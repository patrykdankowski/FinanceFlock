package com.patrykdankowski.financeflock.user.adapter;

import com.patrykdankowski.financeflock.mapper.UserMapper;
import com.patrykdankowski.financeflock.user.dto.UserDtoProjections;
import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.port.UserQueryRepositoryPort;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface UserQueryRepositoryAdapter extends Repository<UserSqlEntity, Long> {

    List<UserSqlEntity> findAllByIdIn(List<Long> ids);

}
@org.springframework.stereotype.Repository
class UserQueryRepositoryImpl implements UserQueryRepositoryPort {
    private final UserQueryRepositoryAdapter userQueryRepository;
    private final UserMapper userMapper;

    UserQueryRepositoryImpl(UserQueryRepositoryAdapter userQueryRepository, UserMapper userMapper) {
        this.userQueryRepository = userQueryRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Set<UserDtoProjections> findAllByShareDataIsTrueAndBudgetGroup_Id(Long budgetGroupID) {
        return Set.of();
    }

    @Override
    public int count() {
        return 0;
    }

    @Override
    public List<UserDomainEntity> findAllByIdIn(List<Long> ids) {
        return userQueryRepository.findAllByIdIn(ids).stream().map(
                userSql -> userMapper.toDomainEntity(userSql)
        ).collect(Collectors.toList());
    }
}
