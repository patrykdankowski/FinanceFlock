package com.patrykdankowski.financeflock.user.adapter;

import com.patrykdankowski.financeflock.user.exception.BadRoleException;
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
    public boolean groupIsNull(final UserDomainEntity user) {
        return user.getBudgetGroupId() == null;
    }

    @Override
    public void validateRole(final UserDomainEntity user, final Role role) {
        if(!user.getRole().equals(role)) {
            throw new BadRoleException(user.getName(), role.toString());
        }
    }
}
