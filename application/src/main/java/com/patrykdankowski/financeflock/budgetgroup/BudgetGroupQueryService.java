package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.UserDtoProjections;
import com.patrykdankowski.financeflock.user.UserDtoResponse;

import java.util.List;

interface BudgetGroupQueryService {

    List<UserDtoResponse> listOfUsersInGroup();

    List<UserDtoProjections> getBudgetGroupExpenses();

}
