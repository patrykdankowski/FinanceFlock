package com.patrykdankowski.financeflock.user.port;

import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;

public interface UserValidatorPort {

    boolean hasGivenRole(UserDomainEntity user, Role role);

    boolean groupIsNull(UserDomainEntity user);

    void validateRole(UserDomainEntity user, Role role);

}
