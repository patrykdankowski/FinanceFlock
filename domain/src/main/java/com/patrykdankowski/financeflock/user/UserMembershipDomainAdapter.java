package com.patrykdankowski.financeflock.user;

import com.patrykdankowski.financeflock.budgetgroup.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.BudgetGroupValidationException;
import com.patrykdankowski.financeflock.budgetgroup.CommonDomainServicePort;
import com.patrykdankowski.financeflock.common.Role;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class UserMembershipDomainAdapter implements UserMembershipDomainPort {

    private final CommonDomainServicePort commonDomainService;

    UserMembershipDomainAdapter(final CommonDomainServicePort commonDomainService) {
        this.commonDomainService = commonDomainService;
    }

    @Override
    public void leaveBudgetGroup(final UserDomainEntity user,
                                 final BudgetGroupDomainEntity budgetGroup,
                                 final Long givenId) {

        log.info("Starting process of leave budget group for user with id {} ", user.getId());

//        commonDomainService.checkIfGroupExists(userFromContext, givenId);

        validateIfUserIsMemberAndRole(user, budgetGroup, givenId, Role.GROUP_MEMBER);

        assignRoleAndBudgetGroupForUser(user, null, Role.USER);

        log.info("User with id {} left budget group", user.getId());

    }

    //    private BudgetGroupDomainEntity validateAndGetBudgetGroup(final UserDomainEntity userFromContext) {
//        BudgetGroupDomainEntity budgetGroupDomainEntityEntity = userFromContext.getBudgetGroupId();
//        if (budgetGroupDomainEntityEntity == null) {
//
//            log.warn("Budget group does not exist for user with id {} ", userFromContext.getId());
//
//            throw new IllegalStateException(userFromContext.getName() + " does not have a group");
//        }
//        return budgetGroupDomainEntityEntity;
//    }
    private void validateIfUserIsMemberAndRole(final UserDomainEntity user,
                                               final BudgetGroupDomainEntity group,
                                               final Long givenId,
                                               final Role role) {
        commonDomainService.checkIfGroupIsNotNull(user);
        commonDomainService.checkIdGroupWithGivenId(givenId, user.getBudgetGroupId());
        final boolean isMember = commonDomainService.checkIfUserIsMemberOfGroup(user, group);
        if (!isMember) {
            throw new BudgetGroupValidationException("User is not a member of budget group");
        }
        commonDomainService.checkRoleForUser(user, role);
    }

    private void assignRoleAndBudgetGroupForUser(final UserDomainEntity userFromContext,
                                                 final Long budgetGroupDomainEntityEntityId,
                                                 final Role role) {
        userFromContext.setRole(role);
        userFromContext.setBudgetGroupId(budgetGroupDomainEntityEntityId);
    }

    @Override
    public boolean toggleShareData(final UserDomainEntity userFromContext) {

        log.info("Starting process of toggle share data");

        toggleShareDataForUser(userFromContext);

        log.info("Toggled share data for user with id {} ", userFromContext.getId());

        return userFromContext.isShareData();
    }

    private void toggleShareDataForUser(final UserDomainEntity userFromContext) {
        userFromContext.setShareData(!userFromContext.isShareData());
    }

}
