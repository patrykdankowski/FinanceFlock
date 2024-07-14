package com.patrykdankowski.financeflock.budgetgroup.port;

import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;

public interface BudgetGroupMembershipDomainPort {

    void addUserToGroup(final UserDomainEntity userFromContext,
                        final UserDomainEntity userToAdd,
                        final Long id,
                        final BudgetGroupDomainEntity budgetGroupDomainEntity);

    void removeUserFromGroup(final UserDomainEntity potentialOwner,
                             final UserDomainEntity userToRemove,
                             final BudgetGroupDomainEntity budgetGroupDomainEntity, final Long id);

}
