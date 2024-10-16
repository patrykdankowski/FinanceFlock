package com.patrykdankowski.financeflock.auth.adapter;


import com.patrykdankowski.financeflock.user.dto.UserDetailsDto;
import com.patrykdankowski.financeflock.user.port.UserQueryRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceAdapterTest {

    @Mock
    private UserQueryRepositoryPort userQueryRepositoryPort;

    @InjectMocks
    private CustomUserDetailsServiceAdapter customUserDetailsServiceAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenUserExists_thenReturnUserDetails() {
        // given
        String email = "test@example.com";
        String password = "password123";
        String role = "ROLE_USER";

        UserDetailsDto userDetailsDto = new UserDetailsDto(email, password, role);
        when(userQueryRepositoryPort.retrieveUserFromEmail(email)).thenReturn(Optional.of(userDetailsDto));

        // when
        UserDetails userDetails = customUserDetailsServiceAdapter.loadUserByUsername(email);

        // then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(email);
        assertThat(userDetails.getPassword()).isEqualTo(password);
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority()).isEqualTo(role);

        verify(userQueryRepositoryPort).retrieveUserFromEmail(email);
    }

    @Test
    void whenUserDoesNotExist_thenThrowUsernameNotFoundException() {
        // given
        String email = "nonexistent@example.com";

        when(userQueryRepositoryPort.retrieveUserFromEmail(email)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> customUserDetailsServiceAdapter.loadUserByUsername(email))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage(email);

        verify(userQueryRepositoryPort).retrieveUserFromEmail(email);
    }

    @Test
    void whenUserExistsWithMultipleRoles_thenReturnUserDetailsWithAuthorities() {
        // given
        String email = "multi.role@example.com";
        String password = "password123";
        String role = "ROLE_ADMIN";

        UserDetailsDto userDetailsDto = new UserDetailsDto(email, password, role);
        when(userQueryRepositoryPort.retrieveUserFromEmail(email)).thenReturn(Optional.of(userDetailsDto));

        // when
        UserDetails userDetails = customUserDetailsServiceAdapter.loadUserByUsername(email);

        // then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(email);
        assertThat(userDetails.getPassword()).isEqualTo(password);
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_ADMIN");

        verify(userQueryRepositoryPort).retrieveUserFromEmail(email);
    }

}