package com.patrykdankowski.financeflock.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${FinanceFlock-secret-key}")
    private String jwtSecretKey;
    @Value("${FinanceFlock-expiration-time}")
    private long jwtExpirationDate;


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

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parse(token);
            return true;
        } catch (MalformedJwtException malformedJwtException) {
            // zrobic customowy exceptiony
            throw new RuntimeException();
            // HttpStatus.Bad.Request, niewła
        } catch (ExpiredJwtException expiredJwtException) {
            throw new RuntimeException();
            //Bad Request, expired jwt token
        } catch (UnsupportedJwtException unsupportedJwtException) {
            throw new RuntimeException();
            // bad request, unsupported token
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new RuntimeException();
            // bad request, jwt claims string is null/empty
        }

    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

}

