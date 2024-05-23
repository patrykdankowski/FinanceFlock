package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.UserDomainEntity;

interface BudgetGroupMembershipDomainPort {

    void addUserToGroup(final UserDomainEntity userFromContext, final UserDomainEntity userToAdd, final Long id);

    void removeUserFromGroup(final UserDomainEntity userFromContext, UserDomainEntity userToRemove, final Long id);

}
