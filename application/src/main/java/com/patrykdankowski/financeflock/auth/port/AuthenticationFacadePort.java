package com.patrykdankowski.financeflock.auth.port;

import com.patrykdankowski.financeflock.auth.dto.JwtAuthenticationResponse;
import com.patrykdankowski.financeflock.auth.dto.LoginDto;
import com.patrykdankowski.financeflock.auth.dto.RegisterDto;
import org.springframework.security.core.Authentication;

public interface AuthenticationFacadePort {

    JwtAuthenticationResponse login(LoginDto loginDto);

    String register(RegisterDto registerDto);

    void updateLastLoginAsync(Authentication authentication);
}
