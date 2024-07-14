package com.patrykdankowski.financeflock.budgetgroup.port;

import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;

public interface BudgetGroupValidatorPort {

    boolean belongsToSameBudgetGroup(UserDomainEntity user, UserDomainEntity otherUser);

    void validateGroupForPotentialOwner(UserDomainEntity potentialOwner,
                                        Long groupId,
                                        BudgetGroupDomainEntity budgetGroupDomainEntity);

    void isAbleToLeaveBudgetGroup(UserDomainEntity loggedUser,
                                  BudgetGroupDomainEntity budgetGroupDomainEntity,
                                  Long groupId);

    void checkIfGroupIsNotNull(UserDomainEntity user);

    void validateSizeOfGroup(BudgetGroupDomainEntity budgetGroup);
}
