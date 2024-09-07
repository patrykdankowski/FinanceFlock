package com.patrykdankowski.financeflock.mapper;

import com.patrykdankowski.financeflock.user.dto.UserLightDto;
import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapperLight {


    public UserLightDto toLightDto(UserSqlEntity userSqlEntity) {
        if (userSqlEntity == null) {
            return null;
        }
        return new UserLightDto(userSqlEntity.getName(), userSqlEntity.getLastLoggedInAt());

    }
}
