package com.patrykdankowski.financeflock.auth.adapter;


import com.patrykdankowski.financeflock.auth.port.TokenProviderFromRequestPort;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class TokenProviderFromRequestAdapterTest {

    private TokenProviderFromRequestPort tokenProviderFromRequest;

    @BeforeEach
    void setUp() {
        tokenProviderFromRequest = new TokenProviderFromRequestAdapter();
    }

    @Test
    void whenValidBearerToken_thenReturnToken() {
        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String bearerToken = "Bearer validToken123";
        when(request.getHeader("Authorization")).thenReturn(bearerToken);

        // when
        String token = tokenProviderFromRequest.getTokenFromRequest(request);

        // then
        assertThat(token).isNotNull();
        assertThat(token).isEqualTo("validToken123");
    }

    @Test
    void whenAuthorizationHeaderIsEmpty_thenReturnNull() {
        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("");

        // when
        String token = tokenProviderFromRequest.getTokenFromRequest(request);

        // then
        assertThat(token).isNull();
    }

    @Test
    void whenAuthorizationHeaderDoesNotStartWithBearer_thenReturnNull() {
        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String invalidToken = "InvalidToken validToken123";
        when(request.getHeader("Authorization")).thenReturn(invalidToken);

        // when
        String token = tokenProviderFromRequest.getTokenFromRequest(request);

        // then
        assertThat(token).isNull();
    }

    @Test
    void whenAuthorizationHeaderIsMissing_thenReturnNull() {
        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        // when
        String token = tokenProviderFromRequest.getTokenFromRequest(request);

        // then
        assertThat(token).isNull();
    }


    @Test
    void whenAuthorizationHeaderWithExtraSpaceAfterBearer_thenReturnToken() {
        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String bearerToken = "Bearer  validToken123";
        when(request.getHeader("Authorization")).thenReturn(bearerToken);

        // when
        String token = tokenProviderFromRequest.getTokenFromRequest(request);

        // then
        assertThat(token).isNotNull();
        assertThat(token).isEqualTo(" validToken123");
    }

    @Test
    void whenAuthorizationHeaderIsTooShort_thenReturnNull() {
        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String shortToken = "Bearer";
        when(request.getHeader("Authorization")).thenReturn(shortToken);

        // when
        String token = tokenProviderFromRequest.getTokenFromRequest(request);

        // then
        assertThat(token).isNull();
    }


}