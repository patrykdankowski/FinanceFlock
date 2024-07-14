package com.patrykdankowski.financeflock.budgetgroup.port;

import com.patrykdankowski.financeflock.user.dto.UserDtoProjections;
import com.patrykdankowski.financeflock.user.model.record.UserDtoResponse;

import java.util.List;

public interface BudgetGroupQueryServicePort {

    List<UserDtoResponse> listOfUsersInGroup();

    List<UserDtoProjections> getBudgetGroupExpenses();

}
