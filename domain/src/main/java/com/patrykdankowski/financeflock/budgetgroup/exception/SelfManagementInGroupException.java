package com.patrykdankowski.financeflock.budgetgroup.exception;

import lombok.Getter;

@Getter
public class SelfManagementInGroupException extends RuntimeException{
    private String exceptionDescription;

   public SelfManagementInGroupException(final String exceptionDescription) {
        this.exceptionDescription = exceptionDescription;
    }
}
