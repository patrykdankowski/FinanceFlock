package com.patrykdankowski.financeflock.auth.port;

import com.patrykdankowski.financeflock.user.dto.SimpleUserDomainEntity;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;

public interface AuthenticationServicePort {
    UserDomainEntity getFullUserFromContext();

    SimpleUserDomainEntity getSimpleUserFromContext();

}
