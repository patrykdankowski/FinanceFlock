package com.patrykdankowski.financeflock.user.port;

import com.patrykdankowski.financeflock.auth.dto.RegisterDtoRequest;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;

public interface UserFactoryPort {

    UserDomainEntity createUserFromRegisterRequest(RegisterDtoRequest registerDtoRequest);
}
