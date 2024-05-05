package com.patrykdankowski.financeflock.user.dto;

import com.patrykdankowski.financeflock.expense.dto.ExpenseProjections;

import java.util.Set;

public interface UserDtoProjections {
    String getName();
    Set<ExpenseProjections> getExpenseList();

}
