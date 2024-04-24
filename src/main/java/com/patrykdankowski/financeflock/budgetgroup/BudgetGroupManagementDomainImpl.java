package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.auth.AuthenticationService;
import com.patrykdankowski.financeflock.constants.Role;
import com.patrykdankowski.financeflock.user.User;
import com.patrykdankowski.financeflock.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.patrykdankowski.financeflock.constants.Role.USER;

@Service
public class BudgetGroupManagementDomainImpl implements BudgetGroupManagementDomain {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final BudgetGroupService budgetGroupService;
    private final BudgetGroupFactory budgetGroupFactory;

    BudgetGroupManagementDomainImpl(final AuthenticationService authenticationService, final UserService userService, final BudgetGroupService budgetGroupService, final BudgetGroupFactory budgetGroupFactory) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.budgetGroupService = budgetGroupService;
        this.budgetGroupFactory = budgetGroupFactory;
    }

    @Override
    public BudgetGroup createBudgetGroup(final BudgetGroupRequest budgetGroupRequest) {
        var userFromContext = authenticationService.getUserFromContext();

        isUserAbleToCreateBudgetGroup(userFromContext);

        BudgetGroup budgetGroup = budgetGroupFactory.createBudgetGroupFromRequest(userFromContext, budgetGroupRequest);
        assignRoleAndBudgetGroupForUser(userFromContext, budgetGroup, Role.GROUP_ADMIN);
        return budgetGroup;
    }

    private static void isUserAbleToCreateBudgetGroup(final User userFromContext) {
        if (userFromContext.getRole() != USER || userFromContext.getBudgetGroup() != null) {
            //TODO CUSTOMOWY EXCEPTION
            throw new IllegalStateException("Cannot create budget group");
        }
    }

    private void assignRoleAndBudgetGroupForUser(final User userFromContext, final BudgetGroup budgetGroup, final Role role) {
        userFromContext.setRole(role);
        userFromContext.setBudgetGroup(budgetGroup);
    }

    @Override
    public GroupUpdateResult closeBudgetGroup() {
        var userFromContext = authenticationService.getUserFromContext();
        BudgetGroup budgetGroup = validateAndGetBudgetGroup(userFromContext);
        validateOwnership(budgetGroup, userFromContext);
        //TODO zaimplementować metode informujaca all userów z grupy ze grupa została zamknięta
        List<User> listOfUsers = resetUsersRolesAndDetachFromGroup(budgetGroup);
        userService.saveAllUsers(listOfUsers);
        budgetGroupService.deleteBudgetGroup(budgetGroup);


        return new GroupUpdateResult(budgetGroup,listOfUsers);
    }

    private List<User> resetUsersRolesAndDetachFromGroup(final BudgetGroup budgetGroup) {
        List<User> listOfUsers = budgetGroup.getListOfMembers().stream().map(
                userToMap ->
                {
                    assignRoleAndBudgetGroupForUser(userToMap, null, USER);
                    return userToMap;
                }).toList();
        return listOfUsers;
    }

    private void validateOwnership(final BudgetGroup budgetGroup, final User userFromContext) {
        if (!budgetGroup.getOwner().equals(userFromContext)) {
            throw new IllegalStateException("Only the group owner can close the group");
        }
    }

    private BudgetGroup validateAndGetBudgetGroup(final User userFromContext) {
        BudgetGroup budgetGroup = userFromContext.getBudgetGroup();
        if (budgetGroup == null) {
            throw new IllegalStateException("There is no group to close");
        }
        return budgetGroup;
    }
}
