package com.patrykdankowski.financeflock.user.dto;

import java.time.LocalDateTime;

public record UserLightDto(String name, LocalDateTime lastLoggedInAt) {
}
