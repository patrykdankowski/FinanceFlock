package com.patrykdankowski.financeflock.budgetgroup.exception;

import lombok.Getter;

@Getter
public class SelfManagementInGroupException extends RuntimeException{
    private String exceptionDescription;

   public SelfManagementInGroupException(final String exceptionDescription) {
       super("You cannot perform this action on yourself in the group.");
        this.exceptionDescription = exceptionDescription;
    }
}
