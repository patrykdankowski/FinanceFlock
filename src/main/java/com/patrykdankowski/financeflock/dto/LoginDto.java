package com.patrykdankowski.financeflock.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class LoginDto {
    private String email;
    private String password;

}
