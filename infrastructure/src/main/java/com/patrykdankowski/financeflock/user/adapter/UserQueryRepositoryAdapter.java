package com.patrykdankowski.financeflock.user.adapter;

import com.patrykdankowski.financeflock.mapper.UserDtoMapper;
import com.patrykdankowski.financeflock.user.dto.UserDetailsDto;
import com.patrykdankowski.financeflock.user.dto.UserDtoProjections;
import com.patrykdankowski.financeflock.user.dto.UserLightDto;
import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;
import com.patrykdankowski.financeflock.user.dto.SimpleUserDomainEntity;
import com.patrykdankowski.financeflock.user.port.UserQueryRepositoryPort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserQueryRepositoryAdapter extends Repository<UserSqlEntity, Long> {


    List<UserLightDto> findAllByBudgetGroup_Id(@Param("groupId") Long groupId, Pageable pageable);

    Optional<UserSqlEntity> findSimpleUserByEmail(String email);

    Optional<UserSqlEntity> findUserDetailsByEmail(String email);
}

@org.springframework.stereotype.Repository
class UserQueryRepositoryImpl implements UserQueryRepositoryPort {
    private final UserQueryRepositoryAdapter userQueryRepository;
    private final UserDtoMapper userDtoMapper;

    UserQueryRepositoryImpl(UserQueryRepositoryAdapter userQueryRepository, final UserDtoMapper userDtoMapper) {
        this.userQueryRepository = userQueryRepository;
        this.userDtoMapper = userDtoMapper;
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
    public List<UserLightDto> findAllByBudgetGroup_Id(Long id, final Pageable pageable) {

        return userQueryRepository.findAllByBudgetGroup_Id(id, pageable);

//        return userQueryRepository.findAllByBudgetGroup_Id(id, pageable).stream().map(
//                userSql -> userMapper.toDomainEntity(userSql)
//        ).collect(Collectors.toList());
    }

    @Override
    public Optional<SimpleUserDomainEntity> retrieveUserFromMail(final String mail) {
        return userQueryRepository.findSimpleUserByEmail(mail).map(user -> userDtoMapper.toSimpleUserDomainEntity(user));
    }

    @Override
    public Optional<UserDetailsDto> retrieveUserFromEmail(final String email) {
        return userQueryRepository.findUserDetailsByEmail(email).map(user -> userDtoMapper.toUserDetailsDto(user));
    }
}
