package com.patrykdankowski.financeflock.controller;

import com.patrykdankowski.financeflock.dto.JwtAuthenticationResponse;
import com.patrykdankowski.financeflock.dto.LoginDto;
import com.patrykdankowski.financeflock.dto.RegisterDto;
import com.patrykdankowski.financeflock.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody LoginDto loginDto) {
        String token = authenticationService.login(loginDto);
        var response = new JwtAuthenticationResponse();
        response.setToken(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDto registerDto) {
        String response = authenticationService.register(registerDto);
        return ResponseEntity.ok(response);
    }

}
