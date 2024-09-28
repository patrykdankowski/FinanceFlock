package com.patrykdankowski.financeflock.auth.dto;

 public record JwtAuthenticationResponse (String token, String tokenType) {

     public JwtAuthenticationResponse(String token) {
         this(token, "Bearer");
     }


}
//package com.patrykdankowski.financeflock.auth.dto;
//
//public class JwtAuthenticationResponse {
//    private String token;
//    private String tokenType = "Bearer";
//
//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(final String token) {
//        this.token = token;
//    }
//
//    public String getTokenType() {
//        return tokenType;
//    }
//
//
//}

