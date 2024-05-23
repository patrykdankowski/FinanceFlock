package com.patrykdankowski.financeflock.auth;

import com.patrykdankowski.financeflock.user.UserDomainEntity;

public interface AuthenticationServicePort {
    UserDomainEntity getUserFromContext();
}
