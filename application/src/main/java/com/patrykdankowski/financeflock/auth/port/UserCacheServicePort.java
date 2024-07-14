package com.patrykdankowski.financeflock.auth.port;

import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;

public interface UserCacheServicePort {

    UserDomainEntity getUserFromEmail(String userEmail);
}
