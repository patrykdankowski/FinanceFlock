package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.AppConstants;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
class BudgetGroupMembershipDomainAdapter implements BudgetGroupMembershipDomainPort {


    private final CommonDomainServicePort commonDomainService;

    BudgetGroupMembershipDomainAdapter(final CommonDomainServicePort commonDomainService) {
        this.commonDomainService = commonDomainService;
    }

    @Override
    public void addUserToGroup(final User userFromContext,
                               final User userToAdd,
                               final Long id) {


        BudgetGroup budgetGroup = commonDomainService.validateAndGetGroup(userFromContext, id);

        validateIfUserIsAdmin(userFromContext);

        // ta metoda wywoluje N+1 selectow
        validateUserIfPossibleToAddToBudgetGroup(budgetGroup, userToAdd);
        addUserToBudgetGroup(budgetGroup, userToAdd);

        assignRoleAndBudgetGroupForUser(userToAdd, budgetGroup, Role.GROUP_MEMBER);


    }

    private void validateUserIfPossibleToAddToBudgetGroup(final BudgetGroup budgetGroup,
                                                          final User userToAdd) {
        if (userToAdd.getBudgetGroup() != null || userToAdd.getRole() != Role.USER) {
            log.warn("User {} is already a member of different group", userToAdd.getName());
            throw new BudgetGroupValidationException("User is already a member of a different group group");
        }
        if (budgetGroup.getListOfMembers().contains(userToAdd)) {
            log.warn("User {} is already a member of this group", userToAdd.getName());
            throw new BudgetGroupValidationException("User is already a member of this group");

        }

        if (budgetGroup.getListOfMembers().size() >= AppConstants.MAX_BUDGET_GROUP_SIZE) {
            log.warn("Budget group reached full size '{}'", AppConstants.MAX_BUDGET_GROUP_SIZE);
            throw new MaxUserCountInBudgetGroupException();
        }

    }

    private void validateIfUserIsAdmin(final User userFromContext) {

        if (userFromContext.getRole() != Role.GROUP_ADMIN || userFromContext.getBudgetGroup() == null) {
            log.error("Security issue: User with ID {} with role {} attempted to add a user to the budget group but is not an admin",
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


        BudgetGroup budgetGroup = commonDomainService.validateAndGetGroup(userFromContext, id);

        validateIsUserAdminOfBudgetGroup(userToRemove, budgetGroup);

        validateUserToRemoveFromBudgetGroup(userToRemove, budgetGroup);

        removeUserFromBudgetGroup(budgetGroup, userToRemove);

    }

    private void validateIsUserAdminOfBudgetGroup(final User userFromContext, final BudgetGroup budgetGroup) {
        if (userFromContext.getRole() != Role.GROUP_ADMIN || !(budgetGroup.getOwner().equals(userFromContext))) {
            log.warn("User {} is not an admin of this group", userFromContext.getName());
            throw new SecurityException("You dont have permission to remove user from the budget group");
        }
    }

    private void validateUserToRemoveFromBudgetGroup(final User userToRemove, final BudgetGroup budgetGroup) {
        if (!(budgetGroup.getListOfMembers().contains(userToRemove)) || userToRemove.getRole() != Role.GROUP_MEMBER) {
            log.warn("User {} is not a member of this group", userToRemove.getName());
            throw new IllegalStateException("User is not a member of the group");
        }
    }

    private void removeUserFromBudgetGroup(final BudgetGroup budgetGroup, final User userToRemove) {
        budgetGroup.getListOfMembers().remove(userToRemove);
        assignRoleAndBudgetGroupForUser(userToRemove, null, Role.USER);
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
