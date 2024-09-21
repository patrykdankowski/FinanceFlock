package com.patrykdankowski.financeflock.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserLight {
    private String name;
    private LocalDateTime lastLoggedInAt;
}
