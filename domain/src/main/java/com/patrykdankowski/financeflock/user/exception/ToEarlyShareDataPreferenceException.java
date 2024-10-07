package com.patrykdankowski.financeflock.user.exception;

import java.time.LocalDateTime;

public class ToEarlyShareDataPreferenceException extends RuntimeException {

    private LocalDateTime lastSharedData;

    public ToEarlyShareDataPreferenceException(LocalDateTime lastSharedData) {
        super("Cannot toggle sharing preference before 5 minutes have passed since: " + lastSharedData.toString());
        this.lastSharedData = lastSharedData;
    }

    public LocalDateTime getLastSharedData() {
        return lastSharedData;
    }

    public LocalDateTime getNextPossibleShareData() {
        return lastSharedData.plusMinutes(5);
    }
}
