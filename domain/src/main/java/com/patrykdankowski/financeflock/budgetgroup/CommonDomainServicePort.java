package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.UserDomainEntity;

public interface CommonDomainServicePort {

    void checkIfGroupExists(final UserDomainEntity userFromContext, final Long id);

    Long checkIfGroupExistsOld(final UserDomainEntity userFromContext);

    void checkIfGroupIsNotNull(final UserDomainEntity user);

    void checkRoleForUser(final UserDomainEntity user,
                          final Role role);

    boolean checkIfUserIsMemberOfGroup(final UserDomainEntity user,
                                       final BudgetGroupDomainEntity group);

    void checkIdGroupWithGivenId(final Long givenId,
                                 final Long idFromUserObject);

    void validateGroupForPotentialOwner(UserDomainEntity userFromContext, Long groupId, BudgetGroupDomainEntity budgetGroupDomainEntity);

}
