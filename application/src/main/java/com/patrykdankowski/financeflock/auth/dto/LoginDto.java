package com.patrykdankowski.financeflock.auth.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginDto {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
