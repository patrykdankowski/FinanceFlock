package com.patrykdankowski.financeflock.dto;

import com.patrykdankowski.financeflock.security.validation.EqualsPassword;
import com.patrykdankowski.financeflock.security.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsPassword
public class RegisterDto {

    @NotNull(message = "Name cannot be null")
    private String name;
    @Email
    private String email;
    @ValidPassword
    private String password;
    @ValidPassword
    private String confirmPassword;
}
