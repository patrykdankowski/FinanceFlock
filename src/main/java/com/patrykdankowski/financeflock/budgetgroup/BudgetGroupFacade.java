package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.auth.AuthenticationService;
import com.patrykdankowski.financeflock.user.User;
import com.patrykdankowski.financeflock.user.UserDtoProjections;
import com.patrykdankowski.financeflock.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.patrykdankowski.financeflock.constants.Role.USER;

@Service
class BudgetGroupFacade {
    private final BudgetGroupMembershipDomain budgetGroupMembershipDomain;
    private final UserService userService;
    private final BudgetGroupService budgetGroupService;
    private final AuthenticationService authenticationService;
    private final BudgetGroupManagementDomain budgetGroupManagementDomain;

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

        final GroupUpdateResult<List<User>> groupUpdateResult = budgetGroupManagementDomain.closeBudgetGroup();

        userService.saveAllUsers(groupUpdateResult.getSource());
        budgetGroupService.deleteBudgetGroup(groupUpdateResult.getBudgetGroup());
    }

    @Transactional
    void addUserToGroup(String email) {

        final BudgetGroup budgetGroup = budgetGroupMembershipDomain.addUserToGroup(email);

        budgetGroupService.saveBudgetGroup(budgetGroup);


    }

    @Transactional
    void removeUserFromGroup(String email) {

        final GroupUpdateResult<User> groupUpdateResult = budgetGroupMembershipDomain.removeUserFromGroup(email);

        budgetGroupService.saveBudgetGroup(groupUpdateResult.getBudgetGroup());
        userService.saveUser(groupUpdateResult.getSource());

    }

    //    @CacheEvict(cacheNames = "userEmailCache", allEntries = true)
//    @Cacheable(cacheNames = "userEmailCache")
//    public List<UserDtoResponse> listOfUsersInGroup() {
//        var userFromContext = authenticationService.getUserFromContext();
//
//
//        BudgetGroup budgetGroup = userFromContext.getBudgetGroup();
//        if (budgetGroup == null) {
//            throw new IllegalStateException(userFromContext.getName() + " is not a member of a group");
//        }
//        return budgetGroupService.findBudgetGroupById(budgetGroup.getId()).map(
//                group -> group.getListOfMembers().stream().map(
//                        user -> new UserDtoResponse(user.getName(), user.getEmail())
//                ).collect(Collectors.toList())).orElseThrow(
//        );
//    }

    List<UserDtoProjections> getBudgetGroupExpenses() {
        var userFromContext = authenticationService.getUserFromContext();

        return new ArrayList<>(userService.findAllUsersByShareDataTrueInSameBudgetGroup(userFromContext.getBudgetGroup().getId()));
    }


}
