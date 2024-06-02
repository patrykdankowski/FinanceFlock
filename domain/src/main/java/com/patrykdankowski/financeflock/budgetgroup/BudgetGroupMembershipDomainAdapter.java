package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.AppConstants;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.UserDomainEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;


@Slf4j
public class BudgetGroupMembershipDomainAdapter implements BudgetGroupMembershipDomainPort {


    private final CommonDomainServicePort commonDomainService;

    BudgetGroupMembershipDomainAdapter(final CommonDomainServicePort commonDomainService) {
        this.commonDomainService = commonDomainService;
    }

    @Override
    public void addUserToGroup(final UserDomainEntity potentialGroupOwner,
                               final UserDomainEntity userToAdd,
                               final Long givenGroupId,
                               final BudgetGroupDomainEntity budgetGroup) {


//        commonDomainService.checkIfGroupExists(userFromContext, potentialGroupId);

//        validateIfUserIsAdmin(userFromContext);

        validateGroup(potentialGroupOwner, budgetGroup, givenGroupId);
        commonDomainService.checkRoleForUser(potentialGroupOwner, Role.GROUP_ADMIN);

        validateUserIfPossibleToAddToBudgetGroup(budgetGroup, userToAdd);
        addUserToBudgetGroup(budgetGroup, userToAdd);

        assignRoleAndBudgetGroupForUser(userToAdd, budgetGroup.getId(), Role.GROUP_MEMBER);


    }

    private void validateGroup(final UserDomainEntity user,
                               final BudgetGroupDomainEntity budgetGroup,
                               final Long givenGroupId) {
        commonDomainService.checkIfGroupIsNotNull(user);
        commonDomainService.checkIdGroupWithGivenId(givenGroupId, user.getBudgetGroupId());
        final boolean isMember = commonDomainService.checkIfUserIsMemberOfGroup(user, budgetGroup);
        if (!isMember) {
            log.warn("User {} is not a member of this group", user.getName());
            throw new IllegalStateException("User is not a member of the group");
        }

    }


    private void validateUserIfPossibleToAddToBudgetGroup(final BudgetGroupDomainEntity budgetGroupDomainEntity,
                                                          final UserDomainEntity userToAdd) {
//        if (userToAdd.getBudgetGroupId() != null || userToAdd.getRole() != Role.USER) {
//            log.warn("User {} is already a member of different group", userToAdd.getName());
//            throw new BudgetGroupValidationException("User is already a member of a different group group");
//        }
//        if (budgetGroupDomainEntity.getListOfMembersId().contains(userToAdd.getId())) {
//            log.warn("User {} is already a member of this group", userToAdd.getName());
//            throw new BudgetGroupValidationException("User is already a member of this group");
//
//        }
        commonDomainService.checkRoleForUser(userToAdd, Role.USER);

        final boolean isMember = commonDomainService.checkIfUserIsMemberOfGroup(userToAdd, budgetGroupDomainEntity);
        if (!isMember) {
            log.warn("User {} is not a member of this group", userToAdd.getName());
            throw new IllegalStateException("User is not a member of the group");
        }


        if (budgetGroupDomainEntity.getListOfMembersId().size() >= AppConstants.MAX_BUDGET_GROUP_SIZE) {
            log.warn("Budget group reached full size '{}'", AppConstants.MAX_BUDGET_GROUP_SIZE);
            throw new MaxUserCountInBudgetGroupException();
        }

    }

//    private void validateIfUserIsAdmin(final UserDomainEntity userFromContext) {
//
//        if (userFromContext.getRole() != Role.GROUP_ADMIN || userFromContext.getBudgetGroupId() == null) {
//            log.error("Security issue: User with ID {} with role {} attempted to add a user to the budget group but is not an admin",
//                    userFromContext.getId(), userFromContext.getRole());
//            throw new SecurityException("You dont have permission to add user to the budget group");
//        }
//
//    }

    private void addUserToBudgetGroup(final BudgetGroupDomainEntity budgetGroupDomainEntity,
                                      final UserDomainEntity userToAdd) {
        budgetGroupDomainEntity.getListOfMembersId().add(userToAdd.getId());
    }

    private void assignRoleAndBudgetGroupForUser(final UserDomainEntity user,
                                                 final Long budgetGroupId,
                                                 final Role role) {
        user.setRole(role);
        user.setBudgetGroupId(budgetGroupId);

    }


    @Override
    public void removeUserFromGroup(final UserDomainEntity potentialGroupOwner,
                                    final UserDomainEntity userToRemove,
                                    final BudgetGroupDomainEntity budgetGroup,
                                    final Long givenGroupId) {


//        commonDomainService.checkIfGroupExists(userFromContext, id);

//        validateIsUserAdminOfBudgetGroup(userToRemove, budgetGroupDomainEntity);

        validateGroup(potentialGroupOwner, budgetGroup, givenGroupId);
        commonDomainService.checkRoleForUser(potentialGroupOwner, Role.GROUP_ADMIN);

        validateUserToRemoveFromBudgetGroup(userToRemove, budgetGroup);
        assignRoleAndBudgetGroupForUser(userToRemove, null, Role.USER);

        removeUserFromBudgetGroup(budgetGroup, userToRemove);

    }

//    private void validateIsUserAdminOfBudgetGroup(final UserDomainEntity userFromContext, final BudgetGroupDomainEntity budgetGroupDomainEntity) {
//        if (userFromContext.getRole() != Role.GROUP_ADMIN || !(budgetGroupDomainEntity.getOwnerId().equals(userFromContext))) {
//            log.warn("User {} is not an admin of this group", userFromContext.getName());
//            throw new SecurityException("You dont have permission to remove user from the budget group");
//        }
//    }


    private void validateUserToRemoveFromBudgetGroup(final UserDomainEntity userToRemove, final BudgetGroupDomainEntity budgetGroupDomainEntity) {
        commonDomainService.checkRoleForUser(userToRemove, Role.GROUP_MEMBER);

        final boolean isMember = commonDomainService.checkIfUserIsMemberOfGroup(userToRemove, budgetGroupDomainEntity);
        if (!isMember) {
            log.warn("User {} is not a member of this group", userToRemove.getName());
            throw new IllegalStateException("User is not a member of the group");
        }


//        if (!(budgetGroupDomainEntity.getListOfMembersId().contains(userToRemove.getId())) || userToRemove.getRole() != Role.GROUP_MEMBER) {
//            log.warn("User {} is not a member of this group", userToRemove.getName());
//            throw new IllegalStateException("User is not a member of the group");
//        }
    }

    private void removeUserFromBudgetGroup(final BudgetGroupDomainEntity budgetGroupDomainEntity, final UserDomainEntity userToRemove) {
        budgetGroupDomainEntity.getListOfMembersId().remove(userToRemove);
//        assignRoleAndBudgetGroupForUser(userToRemove, null, Role.USER);
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
