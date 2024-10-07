package com.patrykdankowski.financeflock.auth.adapter;
import com.patrykdankowski.financeflock.auth.dto.JwtAuthenticationResponse;
import com.patrykdankowski.financeflock.auth.dto.LoginDto;
import com.patrykdankowski.financeflock.auth.dto.RegisterDto;
import com.patrykdankowski.financeflock.auth.port.AuthenticationFacadePort;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
class AuthenticationControllerAdapterTest {

    @Mock
    private AuthenticationFacadePort authenticationFacade;

    @InjectMocks
    private AuthenticationControllerAdapter authenticationControllerAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_shouldReturnSuccessMessage_whenRegistrationIsSuccessful() {
        RegisterDto registerDto = new RegisterDto("John Doe", "user@example.com", "Password123!", "Password123!");
        String successMessage = "User registered successfully.";

        when(authenticationFacade.register(any(RegisterDto.class))).thenReturn(successMessage);

        ResponseEntity<String> response = authenticationControllerAdapter.register(registerDto);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(successMessage);
    }

    @Test
    void register_shouldThrowException_whenPasswordIsTooWeak() {
        RegisterDto registerDto = new RegisterDto("John Doe", "user@example.com", "password123", "password123");

        when(authenticationFacade.register(any(RegisterDto.class)))
                .thenThrow(new IllegalArgumentException("Password must contain at least 1 special character, 1 uppercase letter"));

        try {
            authenticationControllerAdapter.register(registerDto);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Password must contain at least 1 special character, 1 uppercase letter");
        }
    }

    @Test
    void login_shouldReturnJwtResponse_whenLoginIsSuccessful() {
        LoginDto loginDto = new LoginDto("user@example.com", "Password123!");
        JwtAuthenticationResponse jwtResponse = new JwtAuthenticationResponse("jwt-token");

        when(authenticationFacade.login(any(LoginDto.class))).thenReturn(jwtResponse);

        ResponseEntity<JwtAuthenticationResponse> response = authenticationControllerAdapter.login(loginDto);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().token()).isEqualTo("jwt-token");
    }

    @Test
    void logout_shouldReturnSuccessMessage_whenLogoutIsSuccessful() {
        String successMessage = "Logged out successfully.";

        ResponseEntity<String> response = authenticationControllerAdapter.logout(null);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(successMessage);
    }
    @Test
    void register_shouldThrowException_whenEmailsDoNotMatch() {
        RegisterDto registerDto = new RegisterDto("John Doe", "user@example.com", "Password123!", "DifferentEmail@example.com");

        when(authenticationFacade.register(any(RegisterDto.class)))
                .thenThrow(new IllegalArgumentException("Email addresses do not match"));

        assertThatThrownBy(() -> authenticationControllerAdapter.register(registerDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email addresses do not match");
    }

    @Test
    void register_shouldThrowException_whenEmailIsInvalid() {
        RegisterDto registerDto = new RegisterDto("John Doe", "invalid-email", "Password123!", "Password123!");

        when(authenticationFacade.register(any(RegisterDto.class)))
                .thenThrow(new IllegalArgumentException("Invalid email address format"));

        assertThatThrownBy(() -> authenticationControllerAdapter.register(registerDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid email address format");
    }

    @Test
    void login_shouldThrowException_whenCredentialsAreInvalid() {
        LoginDto loginDto = new LoginDto("user@example.com", "WrongPassword");

        when(authenticationFacade.login(any(LoginDto.class)))
                .thenThrow(new IllegalArgumentException("Invalid email or password"));

        assertThatThrownBy(() -> authenticationControllerAdapter.login(loginDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid email or password");
    }

    @Test
    void login_shouldThrowException_whenEmailIsNotRegistered() {
        LoginDto loginDto = new LoginDto("notregistered@example.com", "Password123!");

        when(authenticationFacade.login(any(LoginDto.class)))
                .thenThrow(new IllegalArgumentException("Email is not registered"));

        assertThatThrownBy(() -> authenticationControllerAdapter.login(loginDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email is not registered");
    }

    @Test
    void register_shouldThrowException_whenUserAlreadyExists() {
        RegisterDto registerDto = new RegisterDto("John Doe", "user@example.com", "Password123!", "Password123!");

        when(authenticationFacade.register(any(RegisterDto.class)))
                .thenThrow(new IllegalArgumentException("User with this email already exists"));

        assertThatThrownBy(() -> authenticationControllerAdapter.register(registerDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User with this email already exists");
    }

    @Test
    void logout_shouldThrowException_whenRequestIsNull() {

        doThrow(new NullPointerException("HttpServletRequest cannot be null"))
                .when(authenticationFacade).logout(null);

        assertThatThrownBy(() -> authenticationControllerAdapter.logout(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("HttpServletRequest cannot be null");
    }

    @Test
    void logout_shouldThrowException_whenUserIsNotLoggedIn() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        doThrow(new IllegalStateException("User is not logged in"))
                .when(authenticationFacade).logout(any(HttpServletRequest.class));

        assertThatThrownBy(() -> authenticationControllerAdapter.logout(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("User is not logged in");
    }
}