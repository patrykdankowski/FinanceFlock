package com.patrykdankowski.financeflock.auth;

import com.patrykdankowski.financeflock.user.RegisterDtoRequest;
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
class AuthenticationController {

    private final AuthenticationFacade authenticationFacade;

    @PostMapping("/login")
    ResponseEntity<JwtAuthenticationResponse> login(@RequestBody LoginDto loginDto) {
        String token = authenticationFacade.login(loginDto);
        var response = new JwtAuthenticationResponse();
        response.setToken(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    ResponseEntity<String> register(@Valid @RequestBody RegisterDtoRequest registerDtoRequest) {
        String response = authenticationFacade.register(registerDtoRequest);
        return ResponseEntity.ok(response);
    }

}
