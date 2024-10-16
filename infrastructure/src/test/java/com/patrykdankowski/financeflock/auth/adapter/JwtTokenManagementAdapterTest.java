package com.patrykdankowski.financeflock.auth.adapter;


import com.patrykdankowski.financeflock.auth.exception.CustomJwtException;
import com.patrykdankowski.financeflock.auth.port.TokenCommandServicePort;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import javax.crypto.SecretKey;
import java.lang.reflect.Field;
import java.security.Key;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JwtTokenManagementAdapterTest {

    @Mock
    private TokenCommandServicePort tokenCommandService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JwtTokenManagementAdapter jwtTokenManagementAdapter;

    private static final String SECRET_KEY = "YXNkZmFzZGZhc2RmYXNkZmFzZGZhc2RmYXNkZmFzZGZhc2RmYXNkZmFzZGZh";
    private static final long EXPIRATION_TIME = 3600000;
    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        jwtTokenManagementAdapter = new JwtTokenManagementAdapter(tokenCommandService);


        Field secretKeyField = JwtTokenManagementAdapter.class.getDeclaredField("jwtSecretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtTokenManagementAdapter, SECRET_KEY);

        Field expirationTimeField = JwtTokenManagementAdapter.class.getDeclaredField("jwtExpirationDate");
        expirationTimeField.setAccessible(true);
        expirationTimeField.set(jwtTokenManagementAdapter, EXPIRATION_TIME);
    }

    @Test
    void whenGenerateJwtToken_thenTokenIsGenerated() {
        // given
        String username = "testUser";
        when(authentication.getName()).thenReturn(username);

        // when
        String token = jwtTokenManagementAdapter.generateJwtToken(authentication);

        // then
        assertThat(token).isNotNull();
        verify(authentication).getName();
    }

    @Test
    void whenValidateJwtToken_thenTokenIsValid() {
        // given
        String username = "testUser";

        when(authentication.getName()).thenReturn(username);

        String token = jwtTokenManagementAdapter.generateJwtToken(authentication);

        // when
        boolean isValid = jwtTokenManagementAdapter.validateJwtToken(token);

        // then
        assertThat(isValid).isTrue();
        verify(tokenCommandService).verifyTokenNotRevoked(token);
    }

    @Test
    void whenValidateExpiredJwtToken_thenThrowCustomJwtException() {
        // given
        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date(System.currentTimeMillis() - EXPIRATION_TIME * 2))
                .setExpiration(new Date(System.currentTimeMillis() - EXPIRATION_TIME))
                .signWith(jwtTokenManagementAdapter.key())
                .compact();

        // when & then
        assertThatThrownBy(() -> jwtTokenManagementAdapter.validateJwtToken(token))
                .isInstanceOf(CustomJwtException.class)
                .hasMessage("Expired JWT Token");
    }

    @Test
    void whenValidateJwtTokenWithInvalidSignature_thenThrowCustomJwtException() {
        // given
        String token = Jwts.builder()
                .setSubject("testUser")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS256))
                .compact();

        // when & then
        assertThatThrownBy(() -> jwtTokenManagementAdapter.validateJwtToken(token))
                .isInstanceOf(CustomJwtException.class)
                .hasMessage("The token's signature did not match the expected signature");
    }

    @Test
    void whenGetUsernameFromJwtToken_thenReturnUsername() {
        // given
        String username = "testUser";
        when(authentication.getName()).thenReturn(username);

        String token = jwtTokenManagementAdapter.generateJwtToken(authentication);

        // when
        String extractedUsername = jwtTokenManagementAdapter.getUsernameFromJwtToken(token);

        // then
        assertThat(extractedUsername).isEqualTo(username);
    }

    @Test
    void whenDeactivateToken_thenInvokeRevokeMethod() {
        // given
        String token = "test-jwt-token";

        // when
        jwtTokenManagementAdapter.deactivateToken(token);

        // then
        verify(tokenCommandService).revokeToken(token);
    }

    @Test
    void whenGetKey_thenReturnValidSecretKey() {
        // when
        Key key = jwtTokenManagementAdapter.key();

        // then
        assertThat(key).isNotNull();
        assertThat(key).isInstanceOf(SecretKey.class);
    }

    @Test
    void whenValidateNullJwtToken_thenThrowCustomJwtException() {
        // given
        String token = null;

        // when & then
        assertThatThrownBy(() -> jwtTokenManagementAdapter.validateJwtToken(token))
                .isInstanceOf(CustomJwtException.class)
                .hasMessage("JWT claims is null or empty");
    }

    @Test
    void whenValidateEmptyJwtToken_thenThrowCustomJwtException() {
        // given
        String token = "";

        // when & then
        assertThatThrownBy(() -> jwtTokenManagementAdapter.validateJwtToken(token))
                .isInstanceOf(CustomJwtException.class)
                .hasMessage("JWT claims is null or empty");
    }

    @Test
    void whenValidateMalformedJwtToken_thenThrowCustomJwtException() {
        // given
        String token = "malformed.token.without.valid.structure";

        // when & then
        assertThatThrownBy(() -> jwtTokenManagementAdapter.validateJwtToken(token))
                .isInstanceOf(CustomJwtException.class)
                .hasMessage("Invalid JWT Token");
    }



    @Test
    void whenVerifyTokenNotRevokedThrowsException_thenThrowCustomJwtException() {
        // given
        String token = jwtTokenManagementAdapter.generateJwtToken(authentication);
        doThrow(new CustomJwtException(HttpStatus.FORBIDDEN, "Token is revoked")).when(tokenCommandService).verifyTokenNotRevoked(token);

        // when & then
        assertThatThrownBy(() -> jwtTokenManagementAdapter.validateJwtToken(token))
                .isInstanceOf(CustomJwtException.class)
                .hasMessage("Token is revoked");
    }
}