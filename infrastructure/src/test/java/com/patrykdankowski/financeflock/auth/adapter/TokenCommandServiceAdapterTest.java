package com.patrykdankowski.financeflock.auth.adapter;

import com.patrykdankowski.financeflock.auth.entity.TokenSqlEntity;
import com.patrykdankowski.financeflock.auth.exception.CustomJwtException;
import com.patrykdankowski.financeflock.auth.port.TokenCommandServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TokenCommandServiceAdapterTest {

    @Mock
    private TokenCommandRepositoryAdapter tokenCommandRepositoryAdapter;

    @InjectMocks
    private TokenCommandServiceAdapter tokenCommandServiceAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void saveToken_shouldSaveNewToken_whenTokenDoesNotExist() {
        String token = "newToken";
        String userEmail = "test@example.com";
        when(tokenCommandRepositoryAdapter.findByUserEmail(userEmail)).thenReturn(Optional.empty());

        tokenCommandServiceAdapter.saveToken(token, userEmail);

        verify(tokenCommandRepositoryAdapter, times(1)).save(any(TokenSqlEntity.class));
    }

    @Test
    void saveToken_shouldUpdateExistingToken_whenTokenAlreadyExists() {
        String token = "updatedToken";
        String userEmail = "test@example.com";
        TokenSqlEntity existingToken = new TokenSqlEntity();
        existingToken.setToken("oldToken");
        existingToken.setUserEmail(userEmail);
        existingToken.setRevoked(true);
        when(tokenCommandRepositoryAdapter.findByUserEmail(userEmail)).thenReturn(Optional.of(existingToken));

        tokenCommandServiceAdapter.saveToken(token, userEmail);

        verify(tokenCommandRepositoryAdapter, times(1)).save(existingToken);
        assertThat(existingToken.getToken()).isEqualTo(token);
        assertThat(existingToken.isRevoked()).isFalse();
    }


    @Test
    void verifyTokenNotRevoked_shouldThrowException_whenTokenIsRevoked() {
        String token = "revokedToken";
        TokenSqlEntity tokenEntity = new TokenSqlEntity();
        tokenEntity.setToken(token);
        tokenEntity.setRevoked(true);
        when(tokenCommandRepositoryAdapter.findByToken(token)).thenReturn(Optional.of(tokenEntity));

        assertThatThrownBy(() -> tokenCommandServiceAdapter.verifyTokenNotRevoked(token))
                .isInstanceOf(CustomJwtException.class)
                .hasMessageContaining("Expired JWT Token")
                .extracting("httpStatus")
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void verifyTokenNotRevoked_shouldThrowException_whenTokenNotFound() {
        String token = "nonexistentToken";
        when(tokenCommandRepositoryAdapter.findByToken(token)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tokenCommandServiceAdapter.verifyTokenNotRevoked(token))
                .isInstanceOf(CustomJwtException.class)
                .hasMessageContaining("Token not found")
                .extracting("httpStatus")
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void verifyTokenNotRevoked_shouldNotThrowException_whenTokenIsNotRevoked() {
        String token = "validToken";
        TokenSqlEntity tokenEntity = new TokenSqlEntity();
        tokenEntity.setToken(token);
        tokenEntity.setRevoked(false);
        when(tokenCommandRepositoryAdapter.findByToken(token)).thenReturn(Optional.of(tokenEntity));

        tokenCommandServiceAdapter.verifyTokenNotRevoked(token);
    }


    @Test
    void revokeToken_shouldRevokeToken_whenTokenExists() {
        String token = "tokenToRevoke";
        TokenSqlEntity tokenEntity = new TokenSqlEntity();
        tokenEntity.setToken(token);
        tokenEntity.setRevoked(false);
        when(tokenCommandRepositoryAdapter.findByToken(token)).thenReturn(Optional.of(tokenEntity));

        tokenCommandServiceAdapter.revokeToken(token);

        verify(tokenCommandRepositoryAdapter, times(1)).save(tokenEntity);
        assertThat(tokenEntity.isRevoked()).isTrue();
    }

    @Test
    void revokeToken_shouldDoNothing_whenTokenDoesNotExist() {
        String token = "nonexistentToken";
        when(tokenCommandRepositoryAdapter.findByToken(token)).thenReturn(Optional.empty());

        tokenCommandServiceAdapter.revokeToken(token);

        verify(tokenCommandRepositoryAdapter, never()).save(any(TokenSqlEntity.class));
    }
}