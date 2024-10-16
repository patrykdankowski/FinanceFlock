package com.patrykdankowski.financeflock.auth.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserContextServiceAdapterTest {

    @InjectMocks
    private UserContextServiceAdapter userContextServiceAdapter;

    @BeforeEach
    void setUp() {
        userContextServiceAdapter = new UserContextServiceAdapter();
    }

    @Test
    void whenAuthenticatedUser_thenReturnAuthentication() {
        // given
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            // when
            Authentication result = userContextServiceAdapter.getAuthenticationFromContext();

            // then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(authentication);
        }
    }

    @Test
    void whenNoAuthenticationInContext_thenThrowAuthenticationCredentialsNotFoundException() {
        // given
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(null);

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            // when & then
            assertThatThrownBy(() -> userContextServiceAdapter.getAuthenticationFromContext())
                    .isInstanceOf(AuthenticationCredentialsNotFoundException.class)
                    .hasMessage("No authenticated user found");
        }
    }

    @Test
    void whenAuthenticationIsNotAuthenticated_thenThrowAuthenticationCredentialsNotFoundException() {
        // given
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(false);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            // when & then
            assertThatThrownBy(() -> userContextServiceAdapter.getAuthenticationFromContext())
                    .isInstanceOf(AuthenticationCredentialsNotFoundException.class)
                    .hasMessage("No authenticated user found");
        }
    }
}
