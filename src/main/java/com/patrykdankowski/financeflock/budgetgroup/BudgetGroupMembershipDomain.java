package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.common.UserAndGroupUpdateResult;
import com.patrykdankowski.financeflock.user.UserDtoResponse;

import java.util.List;

public interface BudgetGroupMembershipDomain {

    BudgetGroup addUserToGroup(String email);

    UserAndGroupUpdateResult removeUserFromGroup(String email);

    List<UserDtoResponse> listOfUsersInGroup();

}
