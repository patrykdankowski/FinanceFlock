package com.patrykdankowski.financeflock.user.port;

import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.model.record.UserRegisterVO;

public interface UserFactoryPort {

    UserDomainEntity createUserFromVO(UserRegisterVO registerDto);
}
