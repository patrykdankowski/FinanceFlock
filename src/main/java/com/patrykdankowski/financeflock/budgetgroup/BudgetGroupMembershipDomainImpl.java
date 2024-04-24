package com.patrykdankowski.financeflock.budgetgroup;

import  com.patrykdankowski.financeflock.auth.AuthenticationService;
import com.patrykdankowski.financeflock.constants.Role;
import com.patrykdankowski.financeflock.user.User;
import com.patrykdankowski.financeflock.user.UserDtoResponse;
import com.patrykdankowski.financeflock.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

import static com.patrykdankowski.financeflock.constants.AppConstants.MAX_BUDGET_GROUP_SIZE;
import static com.patrykdankowski.financeflock.constants.Role.USER;

public class BudgetGroupMembershipDomainImpl implements BudgetGroupMembershipDomain {

    private final BudgetGroupService budgetGroupService;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public BudgetGroupMembershipDomainImpl(final BudgetGroupService budgetGroupService,
                                           final UserService userService,
                                           final AuthenticationService authenticationService) {
        this.budgetGroupService = budgetGroupService;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @Override
    public BudgetGroup addUserToGroup(final String email) {
        var userFromContext = authenticationService.getUserFromContext();
        var budgetGroup = validateAndGetBudgetGroup(userFromContext);
        var userToAdd = userService.findUserByEmail(email);


        validateUserIfPossibleToAddToBudgetGroup(budgetGroup, userToAdd);
        addUserToBudgetGroup(budgetGroup, userToAdd);
        assignRoleAndBudgetGroupForUser(userToAdd, budgetGroup, Role.GROUP_MEMBER);


        return budgetGroup;
    }

    private static void assignRoleAndBudgetGroupForUser(final User userToAdd, final BudgetGroup budgetGroup, Role role) {
        userToAdd.setRole(role);
        userToAdd.setBudgetGroup(budgetGroup);

    }

    private static void validateUserIfPossibleToAddToBudgetGroup(final BudgetGroup budgetGroup, final User userToAdd) {
        if (budgetGroup.getListOfMembers().contains(userToAdd)) {
            throw new IllegalStateException("User is already a member of the group");
        }
        if (budgetGroup.getListOfMembers().size() >= MAX_BUDGET_GROUP_SIZE) {
            throw new IllegalStateException("Budget group size is full, remove someone first");
        }
    }

    private static void addUserToBudgetGroup(final BudgetGroup budgetGroup, final User userToAdd) {
        budgetGroup.getListOfMembers().add(userToAdd);
    }

    private static BudgetGroup validateAndGetBudgetGroup(final User userFromContext) {
        var groupToValidate = userFromContext.getBudgetGroup();
        if (groupToValidate == null) {
            throw new IllegalStateException("User does not belong to any budget group");
        }
        return groupToValidate;
    }


    @Override
    public void removeUserFromGroup(final String email) {
        var userFromContext = authenticationService.getUserFromContext();


        BudgetGroup budgetGroup = userFromContext.getBudgetGroup();
        if (budgetGroup == null) {
            throw new IllegalStateException(userFromContext.getName() + " is not a member of a group");
        }
        var userToRemove = userService.findUserByEmail(email);
        if (!(budgetGroup.getListOfMembers().contains(userToRemove))) {
            throw new IllegalStateException("User is not a member of the group");
        }
        budgetGroup.getListOfMembers().remove(userToRemove);
        userToRemove.setRole(USER);
        userToRemove.setBudgetGroup(null);
        budgetGroupService.saveBudgetGroup(budgetGroup);
    }

    @Override
    public List<UserDtoResponse> listOfUsersInGroup() {
        var userFromContext = authenticationService.getUserFromContext();


        BudgetGroup budgetGroup = userFromContext.getBudgetGroup();
        if (budgetGroup == null) {
            throw new IllegalStateException(userFromContext.getName() + " is not a member of a group");
        }
        return budgetGroupService.findBudgetGroupById(budgetGroup.getId()).map(
                group -> group.getListOfMembers().stream().map(
                        user -> new UserDtoResponse(user.getName(), user.getEmail())
                ).collect(Collectors.toList())).orElseThrow(
        );
    }
}
