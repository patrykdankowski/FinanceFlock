package com.patrykdankowski.financeflock.user;

import com.patrykdankowski.financeflock.expense.ExpenseProjections;

import java.util.Set;

public interface UserDtoProjections {
    String getName();
    Set<ExpenseProjections> getExpenseList();

}
