package com.patrykdankowski.financeflock.user;

import com.patrykdankowski.financeflock.auth.AuthenticationService;
import com.patrykdankowski.financeflock.budgetgroup.BudgetGroup;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.common.UserAndGroupUpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
 class UserMembershipDomainImpl implements UserMembershipDomain {

    private final AuthenticationService authenticationService;

    public UserMembershipDomainImpl(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public UserAndGroupUpdateResult leaveBudgetGroup() {
        final User userFromContext = authenticationService.getUserFromContext();

        log.info("Starting process of leave budget group for user with id {} ", userFromContext.getId());

        final BudgetGroup budgetGroupEntity = validateAndGetBudgetGroup(userFromContext);
        assignRoleAndBudgetGroupForUser(userFromContext, null, Role.USER);

        log.info("User with id {} left budget group", userFromContext.getId());

        return new UserAndGroupUpdateResult<>(budgetGroupEntity, userFromContext);
    }

    @Override
    public User toggleShareData() {

        log.info("Starting process of toggle share data");

        final User userFromContext = authenticationService.getUserFromContext();
        toggleShareDataForUser(userFromContext);

        log.info("Toggled share data for user with id {} ", userFromContext.getId());

        return userFromContext;
    }

    private static void toggleShareDataForUser(final User userFromContext) {
        userFromContext.setShareData(!userFromContext.isShareData());
    }

    private BudgetGroup validateAndGetBudgetGroup(final User userFromContext) {
        BudgetGroup budgetGroupEntity = userFromContext.getBudgetGroup();
        if (budgetGroupEntity == null) {

            log.warn("Budget group does not exist for user with id {} ", userFromContext.getId());

            throw new IllegalStateException(userFromContext.getName() + " does not have a group");
        }
        return budgetGroupEntity;
    }

    private void assignRoleAndBudgetGroupForUser(final User userFromContext, final BudgetGroup budgetGroupEntity, final Role role) {
        userFromContext.setRole(role);
        userFromContext.setBudgetGroup(budgetGroupEntity);
    }

}
