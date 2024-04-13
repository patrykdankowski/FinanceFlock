package com.patrykdankowski.financeflock.dto.projections;

import com.patrykdankowski.financeflock.dto.ExpenseDto;

import java.util.List;
import java.util.Set;

public interface UserDtoProjections {
    String getName();
    Set<UserExpenseProjections> getExpenseList();

}
