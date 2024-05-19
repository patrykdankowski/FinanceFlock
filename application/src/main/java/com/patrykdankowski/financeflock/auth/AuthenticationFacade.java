package com.patrykdankowski.financeflock.auth;

import com.patrykdankowski.financeflock.auth.dto.LoginDto;
import com.patrykdankowski.financeflock.auth.dto.RegisterDtoRequest;
import org.springframework.security.core.Authentication;

 interface AuthenticationFacade {

    String login(LoginDto loginDto);

    String register(RegisterDtoRequest registerDtoRequest);

     void updateLastLoginAsync(Authentication authentication);
}
