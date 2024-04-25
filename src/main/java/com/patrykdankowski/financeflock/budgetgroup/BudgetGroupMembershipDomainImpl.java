package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.auth.AuthenticationService;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.common.UserAndGroupUpdateResult;
import com.patrykdankowski.financeflock.exception.BudgetGroupNotFoundException;
import com.patrykdankowski.financeflock.user.User;
import com.patrykdankowski.financeflock.user.UserDtoResponse;
import com.patrykdankowski.financeflock.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.patrykdankowski.financeflock.common.AppConstants.MAX_BUDGET_GROUP_SIZE;
import static com.patrykdankowski.financeflock.common.Role.USER;

@Service
public class BudgetGroupMembershipDomainImpl implements BudgetGroupMembershipDomain {

    private final BudgetGroupService budgetGroupService;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    BudgetGroupMembershipDomainImpl(final BudgetGroupService budgetGroupService,
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

    private void assignRoleAndBudgetGroupForUser(final User user, final BudgetGroup budgetGroup, final Role role) {
        user.setRole(role);
        user.setBudgetGroup(budgetGroup);

    }

    private void validateUserIfPossibleToAddToBudgetGroup(final BudgetGroup budgetGroup, final User userToAdd) {
        if (budgetGroup.getListOfMembers().contains(userToAdd)) {
            throw new IllegalStateException("User is already a member of the group");
        }
        if (budgetGroup.getListOfMembers().size() >= MAX_BUDGET_GROUP_SIZE) {
            throw new IllegalStateException("Budget group size is full, remove someone first");
        }
    }

    private void addUserToBudgetGroup(final BudgetGroup budgetGroup, final User userToAdd) {
        budgetGroup.getListOfMembers().add(userToAdd);
    }

    private BudgetGroup validateAndGetBudgetGroup(final User userFromContext) {
        var groupToValidate = userFromContext.getBudgetGroup();
        if (groupToValidate == null) {
            throw new IllegalStateException("User does not belong to any budget group");
        }
        return groupToValidate;
    }


    @Override
    public UserAndGroupUpdateResult removeUserFromGroup(final String email) {
        var userFromContext = authenticationService.getUserFromContext();


        BudgetGroup budgetGroup = validateAndGetUserGroup(userFromContext);
        var userToRemove = validateAndGetUserToRemoveFromBudgetGroup(email, budgetGroup);

        removeUserFromBudgetGroup(budgetGroup, userToRemove);
        return new UserAndGroupUpdateResult(budgetGroup, userToRemove);

    }

    private  void removeUserFromBudgetGroup(final BudgetGroup budgetGroup, final User userToRemove) {
        budgetGroup.getListOfMembers().remove(userToRemove);
        assignRoleAndBudgetGroupForUser(userToRemove, null, USER);
    }

    private User validateAndGetUserToRemoveFromBudgetGroup(final String email, final BudgetGroup budgetGroup) {
        var userToRemove = userService.findUserByEmail(email);
        if (!(budgetGroup.getListOfMembers().contains(userToRemove))) {
            throw new IllegalStateException("User is not a member of the group");
        }
        return userToRemove;
    }

    private static BudgetGroup validateAndGetUserGroup(final User userFromContext) {
        BudgetGroup budgetGroup = userFromContext.getBudgetGroup();
        if (budgetGroup == null) {
            throw new IllegalStateException(userFromContext.getName() + " is not a member of a group");
        }
        return budgetGroup;
    }

    @Override
    public List<UserDtoResponse> listOfUsersInGroup() {

        var userFromContext = authenticationService.getUserFromContext();

        BudgetGroup budgetGroup = validateAndGetUserGroup(userFromContext);

        return getListOfMembers(budgetGroup);
    }

    private List<UserDtoResponse> getListOfMembers(final BudgetGroup budgetGroup) {
        return budgetGroupService.findBudgetGroupById(budgetGroup.getId()).map(
                        group -> group.getListOfMembers().stream().map(
                                user -> new UserDtoResponse(user.getName(), user.getEmail())
                        ).collect(Collectors.toList()))
                .orElseThrow(() -> new BudgetGroupNotFoundException(budgetGroup.getId()));
    }
}
