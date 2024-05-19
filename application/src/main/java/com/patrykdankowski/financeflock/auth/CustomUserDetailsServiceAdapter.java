package com.patrykdankowski.financeflock.auth;

import com.patrykdankowski.financeflock.user.UserCommandServicePort;
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

    private final UserCommandServicePort userCommandService;

    CustomUserDetailsServiceAdapter(final UserCommandServicePort userCommandService) {
        this.userCommandService = userCommandService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userCommandService.findUserByEmail(email);

        List<GrantedAuthority> authority = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));


        return new User(user.getEmail(), user.getPassword(), authority);
    }

}
