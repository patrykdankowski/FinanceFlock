package com.patrykdankowski.financeflock.user.adapter;

import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.auth.dto.RegisterDtoRequest;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.port.UserFactoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserFactoryAdapter implements UserFactoryPort {

    private final PasswordEncoder passwordEncoder;

    UserFactoryAdapter(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDomainEntity createUserFromRegisterRequest(RegisterDtoRequest registerDtoRequest) {


        UserDomainEntity userDomainEntity = new UserDomainEntity(null,
                registerDtoRequest.getName(),
                passwordEncoder.encode(registerDtoRequest.getPassword()),
                registerDtoRequest.getEmail(),
                LocalDateTime.now());

        userDomainEntity.changeRole(Role.USER);
        userDomainEntity.initializeShareData();

        return userDomainEntity;
    }


}
