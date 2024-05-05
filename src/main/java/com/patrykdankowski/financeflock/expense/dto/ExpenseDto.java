package com.patrykdankowski.financeflock.expense.dto;

import com.patrykdankowski.financeflock.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@Builder
public class ExpenseDto {

    private Long id;
    private Long ownerId;
    private Long ownerGroupId;
    private UserDto owner;
    private BigDecimal amount;
    private LocalDateTime expenseDate;
    private String description;
    private String location;
}
