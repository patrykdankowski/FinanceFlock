package com.patrykdankowski.financeflock.auth;

import com.patrykdankowski.financeflock.user.User;

public interface UserCacheServicePort {

    User getUserFromEmail(String userEmail);
}
