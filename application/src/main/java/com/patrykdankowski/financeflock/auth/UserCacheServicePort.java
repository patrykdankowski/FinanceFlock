package com.patrykdankowski.financeflock.auth;

import com.patrykdankowski.financeflock.user.UserDomainEntity;

public interface UserCacheServicePort {

    UserDomainEntity getUserFromEmail(String userEmail);
}
