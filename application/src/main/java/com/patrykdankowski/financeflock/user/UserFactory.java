package com.patrykdankowski.financeflock.user;

import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.auth.dto.RegisterDtoRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

    private final PasswordEncoder passwordEncoder;

    UserFactory(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User createUserFromRegisterRequest(RegisterDtoRequest registerDtoRequest) {


        return User.builder()
                .name(registerDtoRequest.getName())
                .email(registerDtoRequest.getEmail())
                .password(passwordEncoder.encode(registerDtoRequest.getPassword()))
                .role(Role.USER)
                .shareData(true)
                .build();
    }




}
