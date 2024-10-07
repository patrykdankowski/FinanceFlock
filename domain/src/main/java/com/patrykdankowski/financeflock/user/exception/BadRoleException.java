package com.patrykdankowski.financeflock.user.exception;

import lombok.Getter;

@Getter
public class BadRoleException extends RuntimeException{
    private String name;
    private String roleName;

    public BadRoleException(String name, String roleName) {
        super("Bad role");
        this.name = name;
        this.roleName = roleName;
    }

}
