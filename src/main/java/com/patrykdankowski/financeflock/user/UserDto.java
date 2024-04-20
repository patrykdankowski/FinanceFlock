package com.patrykdankowski.financeflock.user;

public class UserDto {
    private String name;
    private String email;

    public UserDto(User source) {
        this.name = source.getName();
        this.email = source.getEmail();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
