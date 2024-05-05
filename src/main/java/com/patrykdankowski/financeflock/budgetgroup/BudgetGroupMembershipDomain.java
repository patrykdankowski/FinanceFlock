package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.common.UserAndGroupUpdateResult;
import com.patrykdankowski.financeflock.user.dto.UserDtoResponse;

import java.util.List;

 interface BudgetGroupMembershipDomain {

    BudgetGroup addUserToGroup(String email);

    UserAndGroupUpdateResult removeUserFromGroup(String email);

    List<UserDtoResponse> listOfUsersInGroup();

}
