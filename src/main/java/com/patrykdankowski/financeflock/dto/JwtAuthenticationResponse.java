package com.patrykdankowski.financeflock.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthenticationResponse {
    private String token;
    private String tokenType = "Bearer";
}
