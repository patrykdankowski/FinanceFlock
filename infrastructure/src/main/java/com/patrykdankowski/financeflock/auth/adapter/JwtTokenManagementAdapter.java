package com.patrykdankowski.financeflock.auth.adapter;

import com.patrykdankowski.financeflock.auth.exception.CustomJwtException;
import com.patrykdankowski.financeflock.auth.port.JwtTokenManagementPort;
import com.patrykdankowski.financeflock.auth.port.TokenCommandServicePort;
import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
class JwtTokenManagementAdapter implements JwtTokenManagementPort {
    @Value("${FinanceFlock-secret-key}")
    private String jwtSecretKey;
    @Value("${FinanceFlock-expiration-time}")
    private long jwtExpirationDate;

    private final TokenCommandServicePort tokenCommandService;

    JwtTokenManagementAdapter(final TokenCommandServicePort tokenCommandService) {
        this.tokenCommandService = tokenCommandService;
    }


    @Override
    public String generateJwtToken(Authentication authentication) {
        String userName = authentication.getName();
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + jwtExpirationDate);


        String token = Jwts.builder()
                .subject(userName)
                .issuedAt(new Date())
                .expiration(expirationDate)
                .signWith(key())
                .compact();
        return token;
    }

    Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
    }

    @Override
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parse(token);

            tokenCommandService.verifyTokenNotRevoked(token);

        } catch (MalformedJwtException malformedJwtException) {
            throw new CustomJwtException(HttpStatus.BAD_REQUEST, "Invalid JWT Token");

        } catch (ExpiredJwtException expiredJwtException) {
            throw new CustomJwtException(HttpStatus.BAD_REQUEST, "Expired JWT Token");

        } catch (UnsupportedJwtException unsupportedJwtException) {
            throw new CustomJwtException(HttpStatus.BAD_REQUEST, "Unsupported JWT Token");

        } catch (IllegalArgumentException illegalArgumentException) {
            throw new CustomJwtException(HttpStatus.BAD_REQUEST, "JWT claims is null or empty");

        } catch (SignatureException signatureException) {
            throw new CustomJwtException(HttpStatus.UNAUTHORIZED, "The token's signature did not match the expected signature");

        } catch (ClaimJwtException claimJwtException) {
            throw new CustomJwtException(HttpStatus.FORBIDDEN, "JWT claims in the token is not valid");
        }
        return true;
    }

    @Override
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    @Override
    public void deactivateToken(String token) {
        tokenCommandService.revokeToken(token);
    }

}

