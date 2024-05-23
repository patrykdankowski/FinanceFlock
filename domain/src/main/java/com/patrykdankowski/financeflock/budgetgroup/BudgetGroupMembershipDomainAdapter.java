package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.AppConstants;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.UserDomainEntity;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class BudgetGroupMembershipDomainAdapter implements BudgetGroupMembershipDomainPort {


    private final CommonDomainServicePort commonDomainService;

    BudgetGroupMembershipDomainAdapter(final CommonDomainServicePort commonDomainService) {
        this.commonDomainService = commonDomainService;
    }

    @Override
    public void addUserToGroup(final UserDomainEntity userFromContext,
                               final UserDomainEntity userToAdd,
                               final Long id) {


        BudgetGroupDomainEntity budgetGroupDomainEntity = commonDomainService.validateAndGetGroup(userFromContext, id);

        validateIfUserIsAdmin(userFromContext);

        // ta metoda wywoluje N+1 selectow
        validateUserIfPossibleToAddToBudgetGroup(budgetGroupDomainEntity, userToAdd);
        addUserToBudgetGroup(budgetGroupDomainEntity, userToAdd);

        assignRoleAndBudgetGroupForUser(userToAdd, budgetGroupDomainEntity, Role.GROUP_MEMBER);


    }

    private void validateUserIfPossibleToAddToBudgetGroup(final BudgetGroupDomainEntity budgetGroupDomainEntity,
                                                          final UserDomainEntity userToAdd) {
        if (userToAdd.getBudgetGroup() != null || userToAdd.getRole() != Role.USER) {
            log.warn("User {} is already a member of different group", userToAdd.getName());
            throw new BudgetGroupValidationException("User is already a member of a different group group");
        }
        if (budgetGroupDomainEntity.getListOfMembers().contains(userToAdd)) {
            log.warn("User {} is already a member of this group", userToAdd.getName());
            throw new BudgetGroupValidationException("User is already a member of this group");

        }

        if (budgetGroupDomainEntity.getListOfMembers().size() >= AppConstants.MAX_BUDGET_GROUP_SIZE) {
            log.warn("Budget group reached full size '{}'", AppConstants.MAX_BUDGET_GROUP_SIZE);
            throw new MaxUserCountInBudgetGroupException();
        }

    }

    private void validateIfUserIsAdmin(final UserDomainEntity userFromContext) {

        if (userFromContext.getRole() != Role.GROUP_ADMIN || userFromContext.getBudgetGroup() == null) {
            log.error("Security issue: User with ID {} with role {} attempted to add a user to the budget group but is not an admin",
                    userFromContext.getId(), userFromContext.getRole());
            throw new SecurityException("You dont have permission to add user to the budget group");
        }

    }

    private void addUserToBudgetGroup(final BudgetGroupDomainEntity budgetGroupDomainEntity,
                                      final UserDomainEntity userToAdd) {
        budgetGroupDomainEntity.getListOfMembers().add(userToAdd);
    }

    private void assignRoleAndBudgetGroupForUser(final UserDomainEntity user,
                                                 final BudgetGroupDomainEntity budgetGroupDomainEntity,
                                                 final Role role) {
        user.setRole(role);
        user.setBudgetGroup(budgetGroupDomainEntity);

    }


    @Override
    public void removeUserFromGroup(final UserDomainEntity userFromContext,
                                    final UserDomainEntity userToRemove,
                                    final Long id) {


        BudgetGroupDomainEntity budgetGroupDomainEntity = commonDomainService.validateAndGetGroup(userFromContext, id);

        validateIsUserAdminOfBudgetGroup(userToRemove, budgetGroupDomainEntity);

        validateUserToRemoveFromBudgetGroup(userToRemove, budgetGroupDomainEntity);

        removeUserFromBudgetGroup(budgetGroupDomainEntity, userToRemove);

    }

    private void validateIsUserAdminOfBudgetGroup(final UserDomainEntity userFromContext, final BudgetGroupDomainEntity budgetGroupDomainEntity) {
        if (userFromContext.getRole() != Role.GROUP_ADMIN || !(budgetGroupDomainEntity.getOwner().equals(userFromContext))) {
            log.warn("User {} is not an admin of this group", userFromContext.getName());
            throw new SecurityException("You dont have permission to remove user from the budget group");
        }
    }

    private void validateUserToRemoveFromBudgetGroup(final UserDomainEntity userToRemove, final BudgetGroupDomainEntity budgetGroupDomainEntity) {
        if (!(budgetGroupDomainEntity.getListOfMembers().contains(userToRemove)) || userToRemove.getRole() != Role.GROUP_MEMBER) {
            log.warn("User {} is not a member of this group", userToRemove.getName());
            throw new IllegalStateException("User is not a member of the group");
        }
    }

    private void removeUserFromBudgetGroup(final BudgetGroupDomainEntity budgetGroupDomainEntity, final UserDomainEntity userToRemove) {
        budgetGroupDomainEntity.getListOfMembers().remove(userToRemove);
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
