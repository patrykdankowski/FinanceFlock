package com.patrykdankowski.financeflock.common;

import com.patrykdankowski.financeflock.budgetgroup.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.user.UserDomainEntity;

public interface CommonDomainServicePort {

    Long checkIfGroupExistsOld(final UserDomainEntity userFromContext);

    void checkIfGroupIsNotNull(final UserDomainEntity user);

    void checkRoleForUser(final UserDomainEntity user,
                          final Role role);

    boolean checkIfUserIsMemberOfGroup(final UserDomainEntity user,
                                       final BudgetGroupDomainEntity group);

    void checkIdGroupWithGivenId(final Long givenId,
                                 final Long idFromUserObject);

    void validateGroupForPotentialOwner(UserDomainEntity userFromContext,
                                        Long groupId,
                                        BudgetGroupDomainEntity budgetGroupDomainEntity);

    void assignRoleAndBudgetGroupForUser(final UserDomainEntity user,
                                         final Long budgetGroupId,
                                         final Role role);
}
