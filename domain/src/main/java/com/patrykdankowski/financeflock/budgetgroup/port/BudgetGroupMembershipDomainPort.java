package com.patrykdankowski.financeflock.budgetgroup.port;

import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;

public interface BudgetGroupMembershipDomainPort {



    void addUserToGroup(
            UserDomainEntity userToAdd,
            BudgetGroupDomainEntity budgetGroup);

//    void removeUserFromGroup(final UserDomainEntity potentialOwner,
//                             final UserDomainEntity userToRemove,
//                             final BudgetGroupDomainEntity budgetGroupDomainEntity,
//                             final Long id);

    void removeUserFromGroup(
            UserDomainEntity userToRemove,
            BudgetGroupDomainEntity budgetGroup,
            Long givenGroupId);
}
