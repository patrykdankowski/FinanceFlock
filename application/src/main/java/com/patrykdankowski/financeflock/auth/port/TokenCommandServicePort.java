package com.patrykdankowski.financeflock.auth.port;

public interface TokenCommandServicePort {

    void saveToken(String token, String userEmail);
    void verifyTokenNotRevoked (String token);
    void revokeToken(String token);

}
