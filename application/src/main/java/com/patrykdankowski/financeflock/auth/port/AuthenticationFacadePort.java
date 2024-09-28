package com.patrykdankowski.financeflock.auth.port;

import com.patrykdankowski.financeflock.auth.dto.JwtAuthenticationResponse;
import com.patrykdankowski.financeflock.auth.dto.LoginDto;
import com.patrykdankowski.financeflock.auth.dto.RegisterDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface AuthenticationFacadePort {

    JwtAuthenticationResponse login(LoginDto loginDto);

    String register(RegisterDto registerDto);

    void logout(HttpServletRequest request);

//    void updateLastLoginAsync(Authentication authentication);
}
