package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.User;
import com.patrykdankowski.financeflock.user.dto.UserDtoResponse;

import java.util.List;

interface BudgetGroupMembershipDomain {

    void addUserToGroup(final User userFromContext, final User userToAdd, final Long id);

    void removeUserFromGroup(final User userFromContext, User userToRemove, final Long id);

}
