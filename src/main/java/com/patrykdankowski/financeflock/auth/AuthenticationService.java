package com.patrykdankowski.financeflock.auth;

import com.patrykdankowski.financeflock.user.User;

public interface AuthenticationService {
    User getUserFromContext();
}
