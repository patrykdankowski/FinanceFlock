package com.patrykdankowski.financeflock.auth.adapter;


import com.patrykdankowski.financeflock.auth.port.UserCacheServicePort;
import com.patrykdankowski.financeflock.auth.port.UserContextServicePort;
import com.patrykdankowski.financeflock.user.dto.SimpleUserDomainEntity;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AuthenticationServiceAdapterTest {

    @Mock
    private UserContextServicePort userContextService;

    @Mock
    private UserCacheServicePort userCacheService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthenticationServiceAdapter authenticationServiceAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationServiceAdapter = new AuthenticationServiceAdapter(userContextService, userCacheService);
    }


    @Test
    void whenGetFullUserFromContext_thenReturnFullUserDomainEntity() {
        // given
        String userEmail = "test@example.com";
        UserDomainEntity expectedUser = UserDomainEntity.buildUser(1L, "Test User", "password123", userEmail, null);

        when(userContextService.getAuthenticationFromContext()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(userEmail);
        when(userCacheService.getUserFromEmail(userEmail)).thenReturn(expectedUser);

        // when
        UserDomainEntity actualUser = authenticationServiceAdapter.getFullUserFromContext();

        // then
        assertThat(actualUser).isNotNull();
        assertThat(actualUser.getEmail()).isEqualTo(userEmail);
        assertThat(actualUser.getName()).isEqualTo("Test User");

        verify(userContextService).getAuthenticationFromContext();
        verify(authentication).getName();
        verify(userCacheService).getUserFromEmail(userEmail);
    }

    @Test
    void whenGetFullUserFromContextWithNullAuthentication_thenThrowException() {
        // given
        when(userContextService.getAuthenticationFromContext()).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> authenticationServiceAdapter.getFullUserFromContext())
                .isInstanceOf(NullPointerException.class);

        verify(userContextService).getAuthenticationFromContext();
        verifyNoMoreInteractions(userCacheService);
    }


    @Test
    void whenGetSimpleUserFromContext_thenReturnSimpleUserDomainEntity() {
        // given
        String userEmail = "simple@example.com";
        SimpleUserDomainEntity expectedSimpleUser = SimpleUserDomainEntity.buildUser(
                1L,
                "Simple User",
                userEmail,
                null,
                null,
                null,
                false,
                null,
                null
        );

        when(userContextService.getAuthenticationFromContext()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(userEmail);
        when(userCacheService.getSimpleUserFromEmail(userEmail)).thenReturn(expectedSimpleUser);

        // when
        SimpleUserDomainEntity actualSimpleUser = authenticationServiceAdapter.getSimpleUserFromContext();

        // then
        assertThat(actualSimpleUser).isNotNull();
        assertThat(actualSimpleUser.getEmail()).isEqualTo(userEmail);
        assertThat(actualSimpleUser.getName()).isEqualTo("Simple User");

        verify(userContextService).getAuthenticationFromContext();
        verify(authentication).getName();
        verify(userCacheService).getSimpleUserFromEmail(userEmail);
    }

    @Test
    void whenGetSimpleUserFromContextWithInvalidEmail_thenReturnNull() {
        // given
        String userEmail = "invalid@example.com";

        when(userContextService.getAuthenticationFromContext()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(userEmail);
        when(userCacheService.getSimpleUserFromEmail(userEmail)).thenReturn(null);

        // when
        SimpleUserDomainEntity actualSimpleUser = authenticationServiceAdapter.getSimpleUserFromContext();

        // then
        assertThat(actualSimpleUser).isNull();

        verify(userContextService).getAuthenticationFromContext();
        verify(authentication).getName();
        verify(userCacheService).getSimpleUserFromEmail(userEmail);
    }

    @Test
    void whenGetFullUserFromContextWithNonExistentUser_thenReturnNull() {
        // given
        String userEmail = "nonexistent@example.com";

        when(userContextService.getAuthenticationFromContext()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(userEmail);
        when(userCacheService.getUserFromEmail(userEmail)).thenReturn(null);

        // when
        UserDomainEntity actualUser = authenticationServiceAdapter.getFullUserFromContext();

        // then
        assertThat(actualUser).isNull();

        verify(userContextService).getAuthenticationFromContext();
        verify(authentication).getName();
        verify(userCacheService).getUserFromEmail(userEmail);
    }

    @Test
    void whenUserCacheThrowsException_thenHandleGracefully() {
        // given
        String userEmail = "test@example.com";

        when(userContextService.getAuthenticationFromContext()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(userEmail);
        when(userCacheService.getUserFromEmail(userEmail)).thenThrow(new RuntimeException("User service unavailable"));

        // when & then
        assertThatThrownBy(() -> authenticationServiceAdapter.getFullUserFromContext())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User service unavailable");

        verify(userContextService).getAuthenticationFromContext();
        verify(authentication).getName();
        verify(userCacheService).getUserFromEmail(userEmail);
    }

    @Test
    void whenGetFullUserFromContextAndUserNotInCache_thenReturnNull() {
        // given
        String userEmail = "missing@example.com";

        when(userContextService.getAuthenticationFromContext()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(userEmail);
        when(userCacheService.getUserFromEmail(userEmail)).thenReturn(null);

        // when
        UserDomainEntity actualUser = authenticationServiceAdapter.getFullUserFromContext();

        // then
        assertThat(actualUser).isNull();

        verify(userContextService).getAuthenticationFromContext();
        verify(authentication).getName();
        verify(userCacheService).getUserFromEmail(userEmail);
    }

    @Test
    void whenGetSimpleUserFromContextAndUserNotInCache_thenReturnNull() {
        // given
        String userEmail = "missing@example.com";

        when(userContextService.getAuthenticationFromContext()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(userEmail);
        when(userCacheService.getSimpleUserFromEmail(userEmail)).thenReturn(null);

        // when
        SimpleUserDomainEntity actualSimpleUser = authenticationServiceAdapter.getSimpleUserFromContext();

        // then
        assertThat(actualSimpleUser).isNull();

        verify(userContextService).getAuthenticationFromContext();
        verify(authentication).getName();
        verify(userCacheService).getSimpleUserFromEmail(userEmail);
    }

    @Test
    void whenUserContextThrowsException_thenHandleGracefully() {
        // given
        when(userContextService.getAuthenticationFromContext()).thenThrow(new RuntimeException("Context retrieval failed"));

        // when & then
        assertThatThrownBy(() -> authenticationServiceAdapter.getFullUserFromContext())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Context retrieval failed");

        verify(userContextService).getAuthenticationFromContext();
        verifyNoInteractions(userCacheService);
    }

}