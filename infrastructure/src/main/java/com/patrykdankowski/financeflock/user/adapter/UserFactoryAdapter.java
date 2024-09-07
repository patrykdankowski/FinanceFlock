package com.patrykdankowski.financeflock.user.adapter;

import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.model.record.UserRegisterVO;
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
    public UserDomainEntity createUserFromVO(UserRegisterVO registerDto) {


        UserDomainEntity userDomainEntity = UserDomainEntity.buildUser(null,
                registerDto.name(),
                passwordEncoder.encode(registerDto.password()),
                registerDto.email(),
                LocalDateTime.now());

        userDomainEntity.changeRole(Role.USER);
        userDomainEntity.initializeShareData();

        return userDomainEntity;
    }


}
