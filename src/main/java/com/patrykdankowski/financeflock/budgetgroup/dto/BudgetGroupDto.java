package com.patrykdankowski.financeflock.budgetgroup.dto;

import com.patrykdankowski.financeflock.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
public class BudgetGroupDto {

    private Long id;

    private String description;

    private UserDto owner;

    private Set<UserDto> listOfMembers;

}
