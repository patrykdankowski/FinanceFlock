package com.patrykdankowski.financeflock.user.exception;

import java.time.LocalDateTime;

public class ToEarlyShareDataPreferenceException extends RuntimeException {

    private LocalDateTime lastSharedData;

    public ToEarlyShareDataPreferenceException(LocalDateTime lastSharedData) {
        this.lastSharedData = lastSharedData;
    }

    public LocalDateTime getLastSharedData() {
        return lastSharedData;
    }

    public LocalDateTime getNextPossibleShareData() {
        return lastSharedData.plusMinutes(5);
    }
}
