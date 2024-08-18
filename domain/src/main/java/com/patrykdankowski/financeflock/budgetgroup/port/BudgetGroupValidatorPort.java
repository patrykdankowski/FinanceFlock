package com.patrykdankowski.financeflock.budgetgroup.port;

import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;

public interface BudgetGroupValidatorPort {

    boolean belongsToSameBudgetGroup(UserDomainEntity user, UserDomainEntity otherUser);

    void validateGroupForPotentialOwner(UserDomainEntity potentialOwner,
                                        Long groupId,
                                        BudgetGroupDomainEntity budgetGroupDomainEntity);

    void isMemberOfGivenGroup(UserDomainEntity loggedUser,
                              BudgetGroupDomainEntity budgetGroupDomainEntity,
                              Long groupId);


    void validateSizeOfGroup(BudgetGroupDomainEntity budgetGroup);
}
