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

    public UserDomainEntity createUserFromRegisterRequest(RegisterDtoRequest registerDtoRequest) {


//        return UserDomainEntity.builder()
//                .name(registerDtoRequest.getName())
//                .email(registerDtoRequest.getEmail())
//                .password(passwordEncoder.encode(registerDtoRequest.getPassword()))
//                .role(Role.USER)
//                .shareData(true)
//                .build();

        UserDomainEntity userDomainEntity = new UserDomainEntity();
        userDomainEntity.setName(registerDtoRequest.getName());
        userDomainEntity.setEmail(registerDtoRequest.getEmail());
        userDomainEntity.setPassword(passwordEncoder.encode(registerDtoRequest.getPassword()));
        userDomainEntity.setRole(Role.USER);
        userDomainEntity.setShareData(true);
        return userDomainEntity;
    }


}
