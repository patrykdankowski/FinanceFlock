package com.patrykdankowski.financeflock.auth;

import lombok.AllArgsConstructor;

@AllArgsConstructor
 class LoginDto {
    private String email;
    private String password;

     String getEmail() {
        return email;
    }

     String getPassword() {
        return password;
    }
}
