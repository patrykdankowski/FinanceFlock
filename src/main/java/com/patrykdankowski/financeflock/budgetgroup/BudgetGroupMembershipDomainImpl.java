package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.common.CommonDomainService;
import com.patrykdankowski.financeflock.common.Logger;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.exception.BudgetGroupValidateException;
import com.patrykdankowski.financeflock.user.User;
import org.springframework.stereotype.Service;

import static com.patrykdankowski.financeflock.common.AppConstants.MAX_BUDGET_GROUP_SIZE;
import static com.patrykdankowski.financeflock.common.Role.GROUP_ADMIN;
import static com.patrykdankowski.financeflock.common.Role.GROUP_MEMBER;
import static com.patrykdankowski.financeflock.common.Role.USER;

@Service
class BudgetGroupMembershipDomainImpl implements BudgetGroupMembershipDomain {

    private final org.slf4j.Logger logger = Logger.getLogger(this.getClass());

    private final CommonDomainService commonDomainService;

    BudgetGroupMembershipDomainImpl(final CommonDomainService commonDomainService) {
        this.commonDomainService = commonDomainService;
    }

    @Override
    public void addUserToGroup(final User userFromContext,
                               final User userToAdd,
                               final Long id) {



        logger.info("log4");
        BudgetGroup budgetGroup = commonDomainService.validateAndGetGroup(userFromContext, id);
        logger.info("log5");

        validateIfUserIsAdmin(userFromContext);
        logger.info("log6");

        // ta metoda wywoluje N+1 selectow
        validateUserIfPossibleToAddToBudgetGroup(budgetGroup, userToAdd);
        logger.warn("log7");
        addUserToBudgetGroup(budgetGroup, userToAdd);
        logger.info("log8");

        assignRoleAndBudgetGroupForUser(userToAdd, budgetGroup, Role.GROUP_MEMBER);


    }

    private void validateUserIfPossibleToAddToBudgetGroup(final BudgetGroup budgetGroup,
                                                          final User userToAdd) {
        if (userToAdd.getBudgetGroup() != null || userToAdd.getRole() != USER) {
            logger.warn("User {} is already a member of different group", userToAdd.getName());
            throw new BudgetGroupValidateException("User is already a member of a different group group");
        }
        if (budgetGroup.getListOfMembers().contains(userToAdd)) {
            logger.warn("User {} is already a member of this group", userToAdd.getName());
            throw new BudgetGroupValidateException("User is already a member of this group");

        }

        if (budgetGroup.getListOfMembers().size() >= MAX_BUDGET_GROUP_SIZE) {
            logger.warn("Budget group reached full size '{}'", MAX_BUDGET_GROUP_SIZE);
            throw new BudgetGroupValidateException("Budget group size is full, remove someone first");
        }

    }

    private void validateIfUserIsAdmin(final User userFromContext) {

        if (userFromContext.getRole() != Role.GROUP_ADMIN || userFromContext.getBudgetGroup() == null) {
            logger.error("Security issue: User with ID {} with role {} attempted to add a user to the budget group but is not an admin",
                    userFromContext.getId(), userFromContext.getRole());
            throw new SecurityException("You dont have permission to add user to the budget group");
        }

    }

    private void addUserToBudgetGroup(final BudgetGroup budgetGroup,
                                      final User userToAdd) {
        budgetGroup.getListOfMembers().add(userToAdd);
    }

    private void assignRoleAndBudgetGroupForUser(final User user,
                                                 final BudgetGroup budgetGroup,
                                                 final Role role) {
        user.setRole(role);
        user.setBudgetGroup(budgetGroup);

    }


    @Override
    public void removeUserFromGroup(final User userFromContext,
                                    final User userToRemove,
                                    final Long id) {


        BudgetGroup budgetGroup = commonDomainService.validateAndGetGroup(userFromContext,id);

        validateIsUserAdminOfBudgetGroup(userToRemove, budgetGroup);

        validateUserToRemoveFromBudgetGroup(userToRemove, budgetGroup);

        removeUserFromBudgetGroup(budgetGroup, userToRemove);

    }

    private void validateIsUserAdminOfBudgetGroup(final User userFromContext, final BudgetGroup budgetGroup) {
        if (userFromContext.getRole() != GROUP_ADMIN || !(budgetGroup.getOwner().equals(userFromContext))) {
            logger.warn("User {} is not an admin of this group", userFromContext.getName());
            throw new SecurityException("You dont have permission to remove user from the budget group");
        }
    }

    private void validateUserToRemoveFromBudgetGroup(final User userToRemove, final BudgetGroup budgetGroup) {
        if (!(budgetGroup.getListOfMembers().contains(userToRemove)) || userToRemove.getRole() != GROUP_MEMBER) {
            logger.warn("User {} is not a member of this group", userToRemove.getName());
            throw new IllegalStateException("User is not a member of the group");
        }
    }

    private void removeUserFromBudgetGroup(final BudgetGroup budgetGroup, final User userToRemove) {
        budgetGroup.getListOfMembers().remove(userToRemove);
        assignRoleAndBudgetGroupForUser(userToRemove, null, USER);
    }

//    @Override
//    public List<UserDtoResponse> listOfUsersInGroup() {
//
//        var userFromContext = authenticationService.getUserFromContext();
//
//        BudgetGroup budgetGroup = validateAndGetUserGroup(userFromContext);
//
//        return getListOfMembers(budgetGroup);
//    }
//
//    private List<UserDtoResponse> getListOfMembers(final BudgetGroup budgetGroup) {
//        return budgetGroupService.findBudgetGroupById(budgetGroup.getId()).map(
//                        group -> group.getListOfMembers().stream().map(
//                                user -> new UserDtoResponse(user.getName(), user.getEmail())
//                        ).collect(Collectors.toList()))
//                .orElseThrow(() -> new BudgetGroupNotFoundException(budgetGroup.getId()));
//    }
}
