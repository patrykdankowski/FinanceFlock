package com.patrykdankowski.financeflock.user;

import com.patrykdankowski.financeflock.budgetgroup.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.common.Role;
import lombok.extern.slf4j.Slf4j;

@Slf4j
 class UserMembershipDomainAdapter implements UserMembershipDomainPort {

    @Override
    public BudgetGroupDomainEntity leaveBudgetGroup(final UserDomainEntity userFromContext) {

        log.info("Starting process of leave budget group for user with id {} ", userFromContext.getId());

        final BudgetGroupDomainEntity budgetGroupDomainEntityEntity = validateAndGetBudgetGroup(userFromContext);
        assignRoleAndBudgetGroupForUser(userFromContext, null, Role.USER);

        log.info("User with id {} left budget group", userFromContext.getId());

        return budgetGroupDomainEntityEntity;
    }

    private BudgetGroupDomainEntity validateAndGetBudgetGroup(final UserDomainEntity userFromContext) {
        BudgetGroupDomainEntity budgetGroupDomainEntityEntity = userFromContext.getBudgetGroup();
        if (budgetGroupDomainEntityEntity == null) {

            log.warn("Budget group does not exist for user with id {} ", userFromContext.getId());

            throw new IllegalStateException(userFromContext.getName() + " does not have a group");
        }
        return budgetGroupDomainEntityEntity;
    }

    private void assignRoleAndBudgetGroupForUser(final UserDomainEntity userFromContext,
                                                 final BudgetGroupDomainEntity budgetGroupDomainEntityEntity,
                                                 final Role role) {
        userFromContext.setRole(role);
        userFromContext.setBudgetGroup(budgetGroupDomainEntityEntity);
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
