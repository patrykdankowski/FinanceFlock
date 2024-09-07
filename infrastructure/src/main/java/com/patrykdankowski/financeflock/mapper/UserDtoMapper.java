package com.patrykdankowski.financeflock.mapper;

import com.patrykdankowski.financeflock.user.dto.UserDetailsDto;
import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;
import com.patrykdankowski.financeflock.user.dto.SimpleUserDomainEntity;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper {

    public SimpleUserDomainEntity toSimpleUserDomainEntity(UserSqlEntity userSqlEntity) {
        if (userSqlEntity == null) {
            return null;
        } else {
            return SimpleUserDomainEntity.buildUser(userSqlEntity.getId(),
                    userSqlEntity.getName(),
                    userSqlEntity.getEmail(),
                    userSqlEntity.getBudgetGroup().getId(),
                    userSqlEntity.getRole(),
                    userSqlEntity.getLastLoggedInAt(),
                    userSqlEntity.isShareData(),
                    userSqlEntity.getLastToggledShareData(),
                    userSqlEntity.getCreatedAt());
        }
    }

    public UserDetailsDto toUserDetailsDto(UserSqlEntity userSqlEntity) {
        if (userSqlEntity == null) {
            return null;
        } else {
            return new UserDetailsDto(userSqlEntity.getEmail(), userSqlEntity.getPassword(), userSqlEntity.getRole().toString());
        }
    }
}
