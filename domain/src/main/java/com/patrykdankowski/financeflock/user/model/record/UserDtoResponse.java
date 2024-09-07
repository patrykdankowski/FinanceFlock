package com.patrykdankowski.financeflock.user.model.record;

import java.time.LocalDateTime;

public record UserDtoResponse(String name, LocalDateTime lastLoggedInAt) {
}
