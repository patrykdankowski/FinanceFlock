package com.patrykdankowski.financeflock.user.adapter;

import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.port.UserValidatorPort;
import org.springframework.stereotype.Component;

@Component
class UserValidatorAdapter implements UserValidatorPort {
    @Override
    public boolean hasGivenRole(final UserDomainEntity loggedUser, final Role role) {
        return loggedUser.getRole().equals(role);

    }

    @Override
    public boolean groupIsNull(final UserDomainEntity userToAdd) {
        return userToAdd.getBudgetGroupId() == null;
    }
}
