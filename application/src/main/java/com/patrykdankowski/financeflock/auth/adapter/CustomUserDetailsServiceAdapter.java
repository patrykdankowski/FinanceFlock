package com.patrykdankowski.financeflock.auth.adapter;

import com.patrykdankowski.financeflock.auth.dto.CustomUserDetails;
import com.patrykdankowski.financeflock.user.dto.SimpleUserDomainEntity;
import com.patrykdankowski.financeflock.user.dto.UserDetailsDto;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.port.UserCommandRepositoryPort;
import com.patrykdankowski.financeflock.user.port.UserCommandServicePort;
import com.patrykdankowski.financeflock.user.port.UserQueryRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
class CustomUserDetailsServiceAdapter implements UserDetailsService {

    private final UserCommandRepositoryPort userCommandRepositoryPort;

    public CustomUserDetailsServiceAdapter(UserCommandRepositoryPort userCommandRepositoryPort) {
        this.userCommandRepositoryPort = userCommandRepositoryPort;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        log.info("test");
        // Pobieramy encję domenową na podstawie adresu email
        UserDomainEntity userDomain = userCommandRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        log.info(userDomain.getRole().toString() + " 2");

        // Tworzymy autoryzacje dla użytkownika
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(userDomain.getRole().toString()));
        log.info("test3");

        // Zwracamy nasz CustomUserDetails, który przechowuje encję domenową
        return new CustomUserDetails(userDomain, authorities);
    }
}
