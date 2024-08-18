package com.patrykdankowski.financeflock.expense.exception;

import lombok.Getter;

@Getter
public class ErrorDuringFetchingLocationFromIpException extends RuntimeException {

    public ErrorDuringFetchingLocationFromIpException() {
    }
}
