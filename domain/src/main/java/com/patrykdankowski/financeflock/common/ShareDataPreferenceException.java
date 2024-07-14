package com.patrykdankowski.financeflock.common;

import java.time.LocalDateTime;

public class ShareDataPreferenceException extends RuntimeException {

    private LocalDateTime lastSharedData;

    public ShareDataPreferenceException(LocalDateTime lastSharedData) {
        this.lastSharedData = lastSharedData;
    }

    public LocalDateTime getLastSharedData() {
        return lastSharedData;
    }

    public LocalDateTime getNextPossibleShareData() {
        return lastSharedData.plusMinutes(5);
    }
}
