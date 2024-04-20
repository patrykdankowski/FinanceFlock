package com.patrykdankowski.financeflock.auth;

 class JwtAuthenticationResponse {
    private String token;
    private String tokenType = "Bearer";

    public String getToken() {
        return token;
    }

     void setToken(final String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }


}
