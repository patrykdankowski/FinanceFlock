package com.patrykdankowski.financeflock.auth.dto;

import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Primary
public class CustomUserDetails implements UserDetails {


    private final UserDomainEntity userDomainEntity;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(UserDomainEntity userDomainEntity, Collection<? extends GrantedAuthority> authorities) {
        this.userDomainEntity = userDomainEntity;
        this.authorities = authorities;
    }

    public UserDomainEntity getUserDomainEntity() {
        return userDomainEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return userDomainEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userDomainEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Zakładamy, że nie mamy logiki wygasania konta
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Zakładamy, że nie mamy logiki blokowania konta
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Zakładamy, że poświadczenia się nie wygasają
    }

    @Override
    public boolean isEnabled() {
        return true; // Zakładamy, że użytkownik jest aktywny
    }
}