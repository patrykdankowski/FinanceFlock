package com.patrykdankowski.financeflock.user.port;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
 class UserLight {
    private String name;
    private LocalDateTime lastLoggedInAt;
}
