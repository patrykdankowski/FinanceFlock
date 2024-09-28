package com.patrykdankowski.financeflock.auth.adapter;

import com.patrykdankowski.financeflock.auth.entity.TokenSqlEntity;
import com.patrykdankowski.financeflock.auth.exception.CustomJwtException;
import com.patrykdankowski.financeflock.auth.port.TokenCommandServicePort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class TokenCommandServiceAdapter implements TokenCommandServicePort {

    private final TokenCommandRepositoryAdapter tokenCommandRepositoryAdapter;

    public TokenCommandServiceAdapter(final TokenCommandRepositoryAdapter tokenCommandRepositoryAdapter) {
        this.tokenCommandRepositoryAdapter = tokenCommandRepositoryAdapter;
    }

    @Override
    public void saveToken(final String token, final String userEmail) {
        {
            Optional<TokenSqlEntity> existingToken = tokenCommandRepositoryAdapter.findByUserEmail(userEmail);
            if (existingToken.isPresent()) {
                // Nadpisujemy token
                TokenSqlEntity tokenEntity = existingToken.get();
                tokenEntity.setToken(token);
                tokenEntity.setRevoked(false); // Możemy resetować flagę revoked, jeśli użytkownik się loguje ponownie
                tokenCommandRepositoryAdapter.save(tokenEntity);
            } else {
                // Tworzymy nowy token
                TokenSqlEntity newToken = new TokenSqlEntity();
                newToken.setToken(token);
                newToken.setUserEmail(userEmail);
                newToken.setRevoked(false);
                tokenCommandRepositoryAdapter.save(newToken);
            }
//        TokenSqlEntity tokenEntity = new TokenSqlEntity();
//        tokenEntity.setToken(token);
//        tokenEntity.setUserEmail(userEmail);
//        tokenEntity.setRevoked(false);
//        tokenCommandRepositoryAdapter.save(tokenEntity);
        }
    }

    @Override
    public void verifyTokenNotRevoked(final String token) {
        final Optional<TokenSqlEntity> tokenSql = tokenCommandRepositoryAdapter.findByToken(token);
        tokenSql.ifPresentOrElse(
                tokenEntity -> {
                    if (tokenEntity.isRevoked()) {
                        throw new CustomJwtException(HttpStatus.BAD_REQUEST, "Expired JWT Token");
                    }

                }, () -> {
                    throw new CustomJwtException(HttpStatus.BAD_REQUEST, "Token not found");
                });
    }


    @Override
    public void revokeToken(final String token) {
        tokenCommandRepositoryAdapter.findByToken(token).ifPresent(tokenEntity -> {
            tokenEntity.setRevoked(true);
            tokenCommandRepositoryAdapter.save(tokenEntity);
        });
    }
}
