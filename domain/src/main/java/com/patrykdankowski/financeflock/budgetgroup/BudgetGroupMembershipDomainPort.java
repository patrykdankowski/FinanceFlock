package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.User;

interface BudgetGroupMembershipDomainPort {

    void addUserToGroup(final User userFromContext, final User userToAdd, final Long id);

    void removeUserFromGroup(final User userFromContext, User userToRemove, final Long id);

}
