package com.patrykdankowski.financeflock.user.dto;

import com.patrykdankowski.financeflock.common.Role;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Getter
public class UserDto {

    private Long id;

    private String name;

    private LocalDateTime lastLoggedInAt;

    private Role role;

    private Long budgetGroupId;

    private Set<Long> expenseListId;

    private boolean shareData;

}
