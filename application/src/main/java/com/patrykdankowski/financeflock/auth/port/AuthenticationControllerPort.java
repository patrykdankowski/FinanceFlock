package com.patrykdankowski.financeflock.auth.port;

import com.patrykdankowski.financeflock.auth.dto.JwtAuthenticationResponse;
import com.patrykdankowski.financeflock.auth.dto.LoginDto;
import com.patrykdankowski.financeflock.auth.dto.RegisterDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationControllerPort {

    ResponseEntity<JwtAuthenticationResponse> login(LoginDto loginDto);

    ResponseEntity<String> register(RegisterDto registerDto);

    ResponseEntity<String> logout(HttpServletRequest request);
}
