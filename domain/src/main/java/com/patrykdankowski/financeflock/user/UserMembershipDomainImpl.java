package com.patrykdankowski.financeflock.user;

import com.patrykdankowski.financeflock.budgetgroup.BudgetGroup;
import com.patrykdankowski.financeflock.common.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class UserMembershipDomainImpl implements UserMembershipDomain {

    @Override
    public BudgetGroup leaveBudgetGroup(final User userFromContext) {

        log.info("Starting process of leave budget group for user with id {} ", userFromContext.getId());

        final BudgetGroup budgetGroupEntity = validateAndGetBudgetGroup(userFromContext);
        assignRoleAndBudgetGroupForUser(userFromContext, null, Role.USER);

        log.info("User with id {} left budget group", userFromContext.getId());

        return budgetGroupEntity;
    }

    private BudgetGroup validateAndGetBudgetGroup(final User userFromContext) {
        BudgetGroup budgetGroupEntity = userFromContext.getBudgetGroup();
        if (budgetGroupEntity == null) {

            log.warn("Budget group does not exist for user with id {} ", userFromContext.getId());

            throw new IllegalStateException(userFromContext.getName() + " does not have a group");
        }
        return budgetGroupEntity;
    }

    private void assignRoleAndBudgetGroupForUser(final User userFromContext,
                                                 final BudgetGroup budgetGroupEntity,
                                                 final Role role) {
        userFromContext.setRole(role);
        userFromContext.setBudgetGroup(budgetGroupEntity);
    }

    @Override
    public boolean toggleShareData(final User userFromContext) {

        log.info("Starting process of toggle share data");

        toggleShareDataForUser(userFromContext);

        log.info("Toggled share data for user with id {} ", userFromContext.getId());

        return userFromContext.isShareData();
    }

    private void toggleShareDataForUser(final User userFromContext) {
        userFromContext.setShareData(!userFromContext.isShareData());
    }

}
