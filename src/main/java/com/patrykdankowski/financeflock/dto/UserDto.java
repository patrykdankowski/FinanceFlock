package com.patrykdankowski.financeflock.dto;

import com.patrykdankowski.financeflock.entity.User;
import lombok.Getter;

@Getter
public class UserDto {
    private String name;
    private String email;

    public UserDto(User source) {
        this.name = source.getName();
        this.email = source.getEmail();
    }
}
