package com.patrykdankowski.financeflock.auth.adapter;

import com.patrykdankowski.financeflock.auth.dto.JwtAuthenticationResponse;
import com.patrykdankowski.financeflock.auth.dto.LoginDto;
import com.patrykdankowski.financeflock.auth.dto.RegisterDto;
import com.patrykdankowski.financeflock.auth.port.JwtTokenManagementPort;
import com.patrykdankowski.financeflock.auth.port.TokenProviderFromRequestPort;
import com.patrykdankowski.financeflock.auth.port.TokenCommandServicePort;
import com.patrykdankowski.financeflock.user.exception.UserAlreadyExistsException;
import com.patrykdankowski.financeflock.user.model.record.UserRegisterVO;
import com.patrykdankowski.financeflock.user.port.UserCommandServicePort;
import com.patrykdankowski.financeflock.user.port.UserFactoryPort;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthenticationFacadeAdapterTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenManagementPort jwtTokenManagement;

    @Mock
    private UserCommandServicePort userCommandService;

    @Mock
    private UserFactoryPort userFactory;

    @Mock
    private TokenProviderFromRequestPort tokeProviderFromRequest;

    @Mock
    private TokenCommandServicePort tokenCommandService;

    @InjectMocks
    private AuthenticationFacadeAdapter authenticationFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenLoginWithValidCredentials_thenReturnJwtToken() {
        // given
        LoginDto loginDto = new LoginDto("test@example.com", "password123");
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenManagement.generateJwtToken(authentication)).thenReturn("test-jwt-token");

        // when
        JwtAuthenticationResponse response = authenticationFacade.login(loginDto);

        // then
        assertThat(response).isNotNull();
        assertThat(response.token()).isEqualTo("test-jwt-token");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenManagement).generateJwtToken(authentication);
        verify(tokenCommandService).saveToken("test-jwt-token", "test@example.com");
    }

    @Test
    void whenRegisterUser_thenSaveUserAndReturnConfirmation() {
        // given
        RegisterDto registerDto = new RegisterDto("Test User", "test@example.com", "password123","password123");
        UserRegisterVO userRegisterVO = new UserRegisterVO(registerDto.getName(), registerDto.getEmail(), registerDto.getPassword());
        when(userFactory.createUserFromVO(userRegisterVO)).thenReturn(any());

        // when
        String response = authenticationFacade.register(registerDto);

        // then
        assertThat(response).isEqualTo("Registered");
        verify(userCommandService).checkIfUserExists(registerDto.getEmail());
        verify(userFactory).createUserFromVO(userRegisterVO);
        verify(userCommandService).saveUser(any());
    }

    @Test
    void whenLogout_thenDeactivateToken() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(tokeProviderFromRequest.getTokenFromRequest(request)).thenReturn("test-jwt-token");

        // when
        authenticationFacade.logout(request);

        // then
        verify(jwtTokenManagement).deactivateToken("test-jwt-token");
    }

    @Test
    void whenUpdateLastLoginAsync_thenUpdateLastLogin() {
        // given
        String email = "test@example.com";
        LocalDateTime now = LocalDateTime.now();

        // when
        authenticationFacade.updateLastLoginAsync(now, email);

        // then
        verify(userCommandService).updateLastLoggedInAt(now, email);
    }
    @Test
    void whenLoginWithInvalidCredentials_thenThrowException() {
        // given
        LoginDto loginDto = new LoginDto("wrong@example.com", "wrongpassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // when & then
        assertThatThrownBy(() -> authenticationFacade.login(loginDto))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Bad credentials");
    }
    @Test
    void whenRegisterUserWithExistingEmail_thenThrowException() {
        // given
        RegisterDto registerDto = new RegisterDto("Test User", "existing@example.com", "password123", "password123");
        doThrow(new UserAlreadyExistsException("User already exists")).when(userCommandService).checkIfUserExists(registerDto.getEmail());

        // when & then
        assertThatThrownBy(() -> authenticationFacade.register(registerDto))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("User with email with email already exists");
    }

}