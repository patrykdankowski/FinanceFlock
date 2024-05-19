package com.patrykdankowski.financeflock.auth;

import com.patrykdankowski.financeflock.auth.dto.JwtAuthenticationResponse;
import com.patrykdankowski.financeflock.auth.dto.LoginDto;
import com.patrykdankowski.financeflock.auth.dto.RegisterDtoRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

 interface AuthenticationControllerApi {

    @PostMapping("/login")
     ResponseEntity<JwtAuthenticationResponse> login(@RequestBody LoginDto loginDto);

    @PostMapping("/register")
     ResponseEntity<String> register(@Valid @RequestBody RegisterDtoRequest registerDtoRequest);
}
