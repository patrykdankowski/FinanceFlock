package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.dto.UserDtoProjections;
import com.patrykdankowski.financeflock.user.dto.UserDtoResponse;

import java.util.List;

interface BudgetGroupQueryService {

    List<UserDtoResponse> listOfUsersInGroup();

    List<UserDtoProjections> getBudgetGroupExpenses();

}
