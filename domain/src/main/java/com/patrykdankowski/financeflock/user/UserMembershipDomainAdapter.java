package com.patrykdankowski.financeflock.user;

import com.patrykdankowski.financeflock.budgetgroup.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.BudgetGroupValidationException;
import com.patrykdankowski.financeflock.common.CommonDomainServicePort;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.common.ShareDataPreferenceException;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
class UserMembershipDomainAdapter implements UserMembershipDomainPort {

    private final CommonDomainServicePort commonDomainService;

    UserMembershipDomainAdapter(final CommonDomainServicePort commonDomainService) {
        this.commonDomainService = commonDomainService;
    }

    @Override
    public void leaveBudgetGroup(final UserDomainEntity loggedUser,
                                 final BudgetGroupDomainEntity budgetGroup,
                                 final Long givenId) {

        log.info("Starting process of leave budget group for user with id {} ", loggedUser.getId());

//        commonDomainService.checkIfGroupExists(userFromContext, givenId);

        validateIfUserIsMemberAndRole(loggedUser, budgetGroup, givenId, Role.GROUP_MEMBER);

        commonDomainService.assignRoleAndBudgetGroupForUser(loggedUser, null, Role.USER);

        log.info("User with id {} left budget group", loggedUser.getId());

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
    private void validateIfUserIsMemberAndRole(final UserDomainEntity loggedUser,
                                               final BudgetGroupDomainEntity group,
                                               final Long givenId,
                                               final Role role) {
        commonDomainService.checkIfGroupIsNotNull(loggedUser);
        commonDomainService.checkIdGroupWithGivenId(givenId, loggedUser.getBudgetGroupId());
        final boolean isMember = commonDomainService.checkIfUserIsMemberOfGroup(loggedUser, group);
        if (!isMember) {
            throw new BudgetGroupValidationException("User is not a member of budget group");
        }
        commonDomainService.checkRoleForUser(loggedUser, role);
    }


    @Override
    public boolean toggleShareData(final UserDomainEntity loggedUser) {

        log.info("Starting process of toggle share data");

        loggedUser.toggleShareData();


        log.info("Toggled share data for user with id {} ", loggedUser.getId());

        return loggedUser.isShareData();
    }

}
