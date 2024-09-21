package com.patrykdankowski.financeflock.user.exception;

public class BadRoleException extends RuntimeException{
    private String name;
    private String roleName;

    public BadRoleException(String name, String roleName) {
        this.name = name;
        this.roleName = roleName;
    }

    public String getName () {
        return name;
    }

    public String getRoleName() {
        return roleName;
    }
}
