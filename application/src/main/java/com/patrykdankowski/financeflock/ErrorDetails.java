package com.patrykdankowski.financeflock;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@AllArgsConstructor
public class ErrorDetails {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timeStamp;
    private String message;
    private String details;
    private HttpStatus errorCode;

    public Date getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public HttpStatus getErrorCode() {
        return errorCode;
    }
}

