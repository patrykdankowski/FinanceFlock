package com.patrykdankowski.financeflock.common;

public class BadRoleException extends RuntimeException{
    private String name;
    private String roleName;

    public BadRoleException(String name, String roleName) {
        this.name = name;
        this.roleName = roleName;
    }

    public String getMessage() {
        return name;
    }

    public String getRoleName() {
        return roleName;
    }
}
