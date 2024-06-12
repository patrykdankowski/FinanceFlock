package com.patrykdankowski.financeflock.common;

import java.time.LocalDateTime;

public class ShareDataPreferenceException extends RuntimeException {

    private LocalDateTime lastSharedData;
    private LocalDateTime nextPossibleShareData;

    public ShareDataPreferenceException(LocalDateTime lastSharedData) {
        this.lastSharedData = lastSharedData;
        this.nextPossibleShareData = nextPossibleShareData;
    }

    public LocalDateTime getLastSharedData() {
        return lastSharedData;
    }

    public LocalDateTime getNextPossibleShareData() {
        return lastSharedData.plusMinutes(5);
    }
}
