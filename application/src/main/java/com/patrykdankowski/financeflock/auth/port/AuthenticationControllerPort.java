package com.patrykdankowski.financeflock.auth.port;

import com.patrykdankowski.financeflock.auth.dto.JwtAuthenticationResponse;
import com.patrykdankowski.financeflock.auth.dto.LoginDto;
import com.patrykdankowski.financeflock.auth.dto.RegisterDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthenticationControllerPort {

    @PostMapping("/login")
    ResponseEntity<JwtAuthenticationResponse> login(@RequestBody LoginDto loginDto);

    @PostMapping("/register")
    ResponseEntity<String> register(@Valid @RequestBody RegisterDto registerDto);

    @PostMapping("/logout")
    ResponseEntity<String> logout(HttpServletRequest request);
}
