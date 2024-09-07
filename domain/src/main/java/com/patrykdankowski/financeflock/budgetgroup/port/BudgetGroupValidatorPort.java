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

    boolean isMemberOfGivenGroup(final UserDomainEntity user, final BudgetGroupDomainEntity budgetGroup);

    boolean isNotMemberOfAnyGroup(final UserDomainEntity loggedUser);

    void validateIfUserIsAdmin(final UserDomainEntity loggedUser,
                               final Long id, final BudgetGroupDomainEntity budgetGroup);
}
