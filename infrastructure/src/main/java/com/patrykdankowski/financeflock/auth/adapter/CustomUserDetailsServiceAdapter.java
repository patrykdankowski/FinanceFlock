package com.patrykdankowski.financeflock.auth.adapter;

import com.patrykdankowski.financeflock.user.dto.UserDetailsDto;
import com.patrykdankowski.financeflock.user.port.UserQueryRepositoryPort;
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
class CustomUserDetailsServiceAdapter implements UserDetailsService {


    private final UserQueryRepositoryPort userQueryRepositoryPort;

    CustomUserDetailsServiceAdapter(final UserQueryRepositoryPort userQueryRepositoryPort) {
        this.userQueryRepositoryPort = userQueryRepositoryPort;

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserDetailsDto user = userQueryRepositoryPort.retrieveUserFromEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));

        List<GrantedAuthority> authority = Collections.singletonList(new SimpleGrantedAuthority(user.role()));


        return new User(user.email(), user.password(), authority);
    }

}

