package com.patrykdankowski.financeflock.auth;

import com.patrykdankowski.financeflock.auth.dto.JwtAuthenticationResponse;
import com.patrykdankowski.financeflock.auth.dto.LoginDto;
import com.patrykdankowski.financeflock.auth.dto.RegisterDto;
import com.patrykdankowski.financeflock.auth.port.AuthenticationControllerPort;
import com.patrykdankowski.financeflock.auth.port.AuthenticationFacadePort;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
class AuthenticationControllerAdapter implements AuthenticationControllerPort {

    private final AuthenticationFacadePort authenticationFacade;

    public ResponseEntity<JwtAuthenticationResponse> login(LoginDto loginDto) {

        final JwtAuthenticationResponse response = authenticationFacade.login(loginDto);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<String> register(RegisterDto registerDto) {

        final String response = authenticationFacade.register(registerDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        authenticationFacade.logout(request);
        return ResponseEntity.ok("Logged out successfully.");
    }

}


