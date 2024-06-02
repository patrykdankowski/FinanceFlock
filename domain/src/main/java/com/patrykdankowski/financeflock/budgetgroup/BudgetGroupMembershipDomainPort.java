package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.UserDomainEntity;

interface BudgetGroupMembershipDomainPort {

    void addUserToGroup(final UserDomainEntity userFromContext,
                        final UserDomainEntity userToAdd,
                        final Long id,
                        final BudgetGroupDomainEntity budgetGroupDomainEntity);

    void removeUserFromGroup(final UserDomainEntity userFromContext,
                             final UserDomainEntity userToRemove,
                             final BudgetGroupDomainEntity budgetGroupDomainEntity, final Long id);

}
