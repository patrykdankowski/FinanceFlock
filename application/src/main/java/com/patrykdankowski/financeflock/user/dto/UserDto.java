package com.patrykdankowski.financeflock.user.dto;

import com.patrykdankowski.financeflock.expense.dto.ExpenseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {


    private String name;
    private List<ExpenseDto> expenses;
    private BigDecimal totalExpensesForUser;

}
