package com.patrykdankowski.financeflock.auth;

import com.patrykdankowski.financeflock.auth.dto.JwtAuthenticationResponse;
import com.patrykdankowski.financeflock.auth.dto.LoginDto;
import com.patrykdankowski.financeflock.auth.dto.RegisterDtoRequest;
import com.patrykdankowski.financeflock.auth.port.AuthenticationControllerPort;
import com.patrykdankowski.financeflock.auth.port.AuthenticationFacadePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
class AuthenticationControllerAdapter implements AuthenticationControllerPort {

    private final AuthenticationFacadePort authenticationFacade;

    public ResponseEntity<JwtAuthenticationResponse> login(LoginDto loginDto) {
        String token = authenticationFacade.login(loginDto);
        var response = new JwtAuthenticationResponse();
        response.setToken(token);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<String> register(RegisterDtoRequest registerDtoRequest) {
        String response = authenticationFacade.register(registerDtoRequest);
        return ResponseEntity.ok(response);
    }

}


