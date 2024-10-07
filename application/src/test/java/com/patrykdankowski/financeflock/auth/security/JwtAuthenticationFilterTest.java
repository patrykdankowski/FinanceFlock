package com.patrykdankowski.financeflock.auth.security;


import com.patrykdankowski.financeflock.auth.port.JwtTokenManagementPort;
import com.patrykdankowski.financeflock.auth.port.TokenProviderFromRequestPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenManagementPort jwtTokenProvider;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private TokenProviderFromRequestPort tokenProviderFromRequest;

    @Mock
    private HandlerExceptionResolver handlerExceptionResolver;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService, handlerExceptionResolver, tokenProviderFromRequest);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }


    @Test
    void whenValidToken_thenAuthenticateAndContinueFilterChain() throws ServletException, IOException {
        // given
        String token = "valid-token";
        String username = "testUser";
        UserDetails userDetails = new User(username, "", Collections.emptyList());

        when(tokenProviderFromRequest.getTokenFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.validateJwtToken(token)).thenReturn(true);
        when(jwtTokenProvider.getUsernameFromJwtToken(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(tokenProviderFromRequest).getTokenFromRequest(request);
        verify(jwtTokenProvider).validateJwtToken(token);
        verify(jwtTokenProvider).getUsernameFromJwtToken(token);
        verify(userDetailsService).loadUserByUsername(username);
        verify(filterChain).doFilter(request, response);

        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo(userDetails);
    }

    @Test
    void whenNoToken_thenDoNotAuthenticateAndContinueFilterChain() throws ServletException, IOException {
        // given
        when(tokenProviderFromRequest.getTokenFromRequest(request)).thenReturn(null);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(tokenProviderFromRequest).getTokenFromRequest(request);
        verifyNoInteractions(jwtTokenProvider);
        verify(filterChain).doFilter(request, response);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void whenInvalidToken_thenHandleExceptionAndContinueFilterChain() throws ServletException, IOException {
        // given
        String token = "invalid-token";

        when(tokenProviderFromRequest.getTokenFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.validateJwtToken(token)).thenThrow(new RuntimeException("Invalid token"));

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(tokenProviderFromRequest).getTokenFromRequest(request);
        verify(jwtTokenProvider).validateJwtToken(token);
        verify(handlerExceptionResolver).resolveException(eq(request), eq(response), isNull(), any(RuntimeException.class));
        verify(filterChain, never()).doFilter(request, response);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void whenTokenIsValidButExceptionOccurs_thenHandleException() throws ServletException, IOException {
        // given
        String token = "valid-token";
        String username = "testUser";

        when(tokenProviderFromRequest.getTokenFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.validateJwtToken(token)).thenReturn(true);
        when(jwtTokenProvider.getUsernameFromJwtToken(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenThrow(new RuntimeException("User not found"));

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(tokenProviderFromRequest).getTokenFromRequest(request);
        verify(jwtTokenProvider).validateJwtToken(token);
        verify(jwtTokenProvider).getUsernameFromJwtToken(token);
        verify(userDetailsService).loadUserByUsername(username);
        verify(handlerExceptionResolver).resolveException(eq(request), eq(response), isNull(), any(RuntimeException.class));
        verify(filterChain, never()).doFilter(request, response);
    }
    @Test
    void whenEmptyToken_thenDoNotAuthenticateAndContinueFilterChain() throws ServletException, IOException {
        // given
        when(tokenProviderFromRequest.getTokenFromRequest(request)).thenReturn("");

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(tokenProviderFromRequest).getTokenFromRequest(request);
        verifyNoInteractions(jwtTokenProvider);
        verify(filterChain).doFilter(request, response);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void whenExpiredToken_thenHandleExceptionAndDoNotAuthenticate() throws ServletException, IOException {
        // given
        String token = "expired-token";
        when(tokenProviderFromRequest.getTokenFromRequest(request)).thenReturn(token);
        doThrow(new RuntimeException("Expired JWT Token")).when(jwtTokenProvider).validateJwtToken(token);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(tokenProviderFromRequest).getTokenFromRequest(request);
        verify(jwtTokenProvider).validateJwtToken(token);
        verify(handlerExceptionResolver).resolveException(eq(request), eq(response), isNull(), any(RuntimeException.class));
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void whenTokenValidationFails_thenDoNotAuthenticateAndContinueFilterChain() throws ServletException, IOException {
        // given
        String token = "invalid-token";
        when(tokenProviderFromRequest.getTokenFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.validateJwtToken(token)).thenReturn(false);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(tokenProviderFromRequest).getTokenFromRequest(request);
        verify(jwtTokenProvider).validateJwtToken(token);
        verify(filterChain).doFilter(request, response);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void whenTokenIsValidButUserDoesNotExist_thenHandleException() throws ServletException, IOException {
        // given
        String token = "valid-token";
        String username = "nonexistentUser";

        when(tokenProviderFromRequest.getTokenFromRequest(request)).thenReturn(token);
        when(jwtTokenProvider.validateJwtToken(token)).thenReturn(true);
        when(jwtTokenProvider.getUsernameFromJwtToken(token)).thenReturn(username);
        doThrow(new UsernameNotFoundException("User not found")).when(userDetailsService).loadUserByUsername(username);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(tokenProviderFromRequest).getTokenFromRequest(request);
        verify(jwtTokenProvider).validateJwtToken(token);
        verify(jwtTokenProvider).getUsernameFromJwtToken(token);
        verify(userDetailsService).loadUserByUsername(username);
        verify(handlerExceptionResolver).resolveException(eq(request), eq(response), isNull(), any(UsernameNotFoundException.class));
        verify(filterChain, never()).doFilter(request, response);
    }
}