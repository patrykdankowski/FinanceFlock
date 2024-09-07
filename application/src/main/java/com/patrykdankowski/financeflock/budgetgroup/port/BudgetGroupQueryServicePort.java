package com.patrykdankowski.financeflock.budgetgroup.port;

import com.patrykdankowski.financeflock.user.dto.UserDtoProjections;
import com.patrykdankowski.financeflock.user.dto.UserLightDto;

import java.util.List;

public interface BudgetGroupQueryServicePort {

    List<UserLightDto> listOfUsersInGroup(final Long id, final int page, final int size, final String sortBy, final String sortDirection);

    List<UserDtoProjections> getBudgetGroupExpenses();

}
