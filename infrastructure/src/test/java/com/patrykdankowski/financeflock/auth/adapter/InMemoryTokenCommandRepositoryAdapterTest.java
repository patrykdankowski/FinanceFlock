package com.patrykdankowski.financeflock.auth.adapter;

import com.patrykdankowski.financeflock.auth.entity.TokenSqlEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryTokenCommandRepositoryAdapterTest {

    private InMemoryTokenRepository tokenRepository;

    @BeforeEach
    void setUp() {
        tokenRepository = new InMemoryTokenRepository();
    }


    @Test
    void save_shouldPersistTokenEntity() {
        TokenSqlEntity token = new TokenSqlEntity();
        token.setToken("testToken123");
        token.setUserEmail("test@example.com");

        tokenRepository.save(token);

        Optional<TokenSqlEntity> retrievedToken = tokenRepository.findByToken("testToken123");
        assertThat(retrievedToken).isPresent();
        assertThat(retrievedToken.get().getUserEmail()).isEqualTo("test@example.com");
    }

    @Test
    void save_shouldUpdateExistingToken_whenTokenIsSavedWithExistingId() {
        TokenSqlEntity token = new TokenSqlEntity();
        token.setToken("testToken123");
        token.setUserEmail("test@example.com");
        tokenRepository.save(token);

        token.setToken("updatedToken");
        tokenRepository.save(token);

        Optional<TokenSqlEntity> retrievedToken = tokenRepository.findByToken("updatedToken");

        assertThat(retrievedToken).isPresent();
        assertThat(retrievedToken.get().getUserEmail()).isEqualTo("test@example.com");
    }


    @Test
    void findByToken_shouldReturnTokenEntity_whenTokenExists() {
        TokenSqlEntity token = new TokenSqlEntity();
        token.setToken("testToken123");
        token.setUserEmail("test@example.com");
        tokenRepository.save(token);

        Optional<TokenSqlEntity> result = tokenRepository.findByToken("testToken123");

        assertThat(result).isPresent();
        assertThat(result.get().getUserEmail()).isEqualTo("test@example.com");
    }

    @Test
    void findByToken_shouldReturnEmptyOptional_whenTokenDoesNotExist() {
        Optional<TokenSqlEntity> result = tokenRepository.findByToken("nonexistentToken");

        assertThat(result).isNotPresent();
    }

    @Test
    void findByToken_shouldReturnEmptyOptional_whenTokenIsNull() {
        Optional<TokenSqlEntity> result = tokenRepository.findByToken(null);

        assertThat(result).isNotPresent();
    }


    @Test
    void findByUserEmail_shouldReturnTokenEntity_whenTokenExists() {
        TokenSqlEntity token = new TokenSqlEntity();
        token.setToken("testToken123");
        token.setUserEmail("test@example.com");
        tokenRepository.save(token);

        Optional<TokenSqlEntity> result = tokenRepository.findByUserEmail("test@example.com");

        assertThat(result).isPresent();
        assertThat(result.get().getToken()).isEqualTo("testToken123");
    }

    @Test
    void findByUserEmail_shouldReturnEmptyOptional_whenUserEmailDoesNotExist() {
        Optional<TokenSqlEntity> result = tokenRepository.findByUserEmail("nonexistent@example.com");

        assertThat(result).isNotPresent();
    }

    @Test
    void findByUserEmail_shouldReturnEmptyOptional_whenUserEmailIsNull() {
        Optional<TokenSqlEntity> result = tokenRepository.findByUserEmail(null);

        assertThat(result).isNotPresent();
    }


}
