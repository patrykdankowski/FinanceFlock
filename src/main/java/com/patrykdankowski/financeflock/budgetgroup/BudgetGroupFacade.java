package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.auth.AuthenticationService;
import com.patrykdankowski.financeflock.common.UserAndGroupUpdateResult;
import com.patrykdankowski.financeflock.user.User;
import com.patrykdankowski.financeflock.user.UserDtoProjections;
import com.patrykdankowski.financeflock.user.UserDtoResponse;
import com.patrykdankowski.financeflock.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
class BudgetGroupFacade {
    private final BudgetGroupMembershipDomain budgetGroupMembershipDomain;
    private final BudgetGroupManagementDomain budgetGroupManagementDomain;
    private final UserService userService;
    private final BudgetGroupService budgetGroupService;
    private final AuthenticationService authenticationService;

    BudgetGroupFacade(final BudgetGroupMembershipDomain budgetGroupMembershipDomain,
                      final UserService userService,
                      final BudgetGroupService budgetGroupService,
                      final AuthenticationService authenticationService,
                      final BudgetGroupManagementDomain budgetGroupManagementDomain) {
        this.budgetGroupMembershipDomain = budgetGroupMembershipDomain;
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.budgetGroupService = budgetGroupService;
        this.budgetGroupManagementDomain = budgetGroupManagementDomain;
    }

    @Transactional
//    @CacheEvict(cacheNames = "userEmailCache", allEntries = true)
    void createBudgetGroup(BudgetGroupRequest budgetGroupRequest) {

        final BudgetGroup budgetGroup = budgetGroupManagementDomain.createBudgetGroup(budgetGroupRequest);
        budgetGroupService.saveBudgetGroup(budgetGroup);

    }

    @Transactional
//    @CacheEvict(cacheNames = "userEmailCache", allEntries = true)
    void closeBudgetGroup() {

        final UserAndGroupUpdateResult<List<User>> userAndGroupUpdateResult = budgetGroupManagementDomain.closeBudgetGroup();

        userService.saveAllUsers(userAndGroupUpdateResult.getSource());
        budgetGroupService.deleteBudgetGroup(userAndGroupUpdateResult.getBudgetGroup());
    }

    @Transactional
    void addUserToGroup(String email) {

        final BudgetGroup budgetGroup = budgetGroupMembershipDomain.addUserToGroup(email);

        budgetGroupService.saveBudgetGroup(budgetGroup);


    }

    @Transactional
    void removeUserFromGroup(String email) {

        final UserAndGroupUpdateResult<User> userAndGroupUpdateResult = budgetGroupMembershipDomain.removeUserFromGroup(email);

        budgetGroupService.saveBudgetGroup(userAndGroupUpdateResult.getBudgetGroup());
        userService.saveUser(userAndGroupUpdateResult.getSource());

    }


     List<UserDtoResponse> listOfUsersInGroup() {

        return budgetGroupMembershipDomain.listOfUsersInGroup();


    }

    List<UserDtoProjections> getBudgetGroupExpenses() {
        var userFromContext = authenticationService.getUserFromContext();

        return new ArrayList<>(userService.findAllUsersByShareDataTrueInSameBudgetGroup(userFromContext.getBudgetGroup().getId()));
    }


}
