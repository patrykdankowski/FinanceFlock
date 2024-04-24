package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.UserDtoResponse;

import java.util.List;

public interface BudgetGroupMembershipDomain {

    BudgetGroup addUserToGroup(String email);

    GroupUpdateResult removeUserFromGroup(String email);

    List<UserDtoResponse> listOfUsersInGroup();

}
