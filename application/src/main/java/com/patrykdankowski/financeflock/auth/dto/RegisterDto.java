package com.patrykdankowski.financeflock.auth.dto;

import com.patrykdankowski.financeflock.auth.validation.EqualsPassword;
import com.patrykdankowski.financeflock.auth.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@EqualsPassword
public class RegisterDto {

    @NotNull(message = "Name cannot be null")
    @Size(min = 3, message = "Name should be at least 3 letters")
    private String name;
    @Email(message = "Your email is not valid")
    @NotBlank(message = "Email cannot be blank")
    private String email;
    @ValidPassword
    private String password;
    @ValidPassword
    private String confirmPassword;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}
