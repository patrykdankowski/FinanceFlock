package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.auth.AuthenticationService;
import com.patrykdankowski.financeflock.constants.Role;
import com.patrykdankowski.financeflock.user.User;
import com.patrykdankowski.financeflock.user.UserDtoProjections;
import com.patrykdankowski.financeflock.user.UserDtoResponse;
import com.patrykdankowski.financeflock.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.patrykdankowski.financeflock.constants.AppConstants.MAX_BUDGET_GROUP_SIZE;
import static com.patrykdankowski.financeflock.constants.Role.USER;

@Service
class BudgetGroupFacade {
    private final BudgetGroupMembershipDomain budgetGroupMembershipDomain;
    private final UserService userService;
    private final BudgetGroupService budgetGroupService;
    private final AuthenticationService authenticationService;

    BudgetGroupFacade(final BudgetGroupMembershipDomain budgetGroupMembershipDomain, final UserService userService, final BudgetGroupService budgetGroupService, final AuthenticationService authenticationService) {
        this.budgetGroupMembershipDomain = budgetGroupMembershipDomain;
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.budgetGroupService = budgetGroupService;
    }

    @Transactional
//    @CacheEvict(cacheNames = "userEmailCache", allEntries = true)
    void createBudgetGroup(BudgetGroupRequest budgetGroupRequest) {
        var userFromContext = authenticationService.getUserFromContext();

        userFromContext.setRole(Role.GROUP_ADMIN);
        BudgetGroup budgetGroup = BudgetGroup.create(userFromContext, budgetGroupRequest);
        userFromContext.setBudgetGroup(budgetGroup);
        budgetGroupService.saveBudgetGroup(budgetGroup);


    }

    @Transactional
//    @CacheEvict(cacheNames = "userEmailCache", allEntries = true)
    void closeBudgetGroup() {
        var userFromContext = authenticationService.getUserFromContext();
        BudgetGroup budgetGroup = userFromContext.getBudgetGroup();
        if (budgetGroup == null) {
            throw new IllegalStateException("There is no group to close");
        }
        if (!budgetGroup.getOwner().equals(userFromContext)) {
            throw new IllegalStateException("Only the group owner can close the group");
        }
        //TODO zaimplementować metode informujaca all userów z grupy ze grupa została zamknięta
        List<User> listOfUsers = budgetGroup.getListOfMembers().stream().map(
                userToMap ->
                {
                    userToMap.setRole(USER);
                    userToMap.setBudgetGroup(null);
                    return userToMap;
                }).toList();
        userService.saveAllUsers(listOfUsers);
        budgetGroupService.deleteBudgetGroup(budgetGroup);
    }

    @Transactional
    void addUserToGroup(String email) {

        final BudgetGroup budgetGroup = budgetGroupMembershipDomain.addUserToGroup(email);

        budgetGroupService.saveBudgetGroup(budgetGroup);


    }

//    @Transactional
//    void removeUserFromGroup(String email) {
//        var userFromContext = authenticationService.getUserFromContext();
//
//
//        BudgetGroup budgetGroup = userFromContext.getBudgetGroup();
//        if (budgetGroup == null) {
//            throw new IllegalStateException(userFromContext.getName() + " is not a member of a group");
//        }
//        var userToRemove = userService.findUserByEmail(email);
//        if (!(budgetGroup.getListOfMembers().contains(userToRemove))) {
//            throw new IllegalStateException("User is not a member of the group");
//        }
//        budgetGroup.getListOfMembers().remove(userToRemove);
//        userToRemove.setRole(USER);
//        userToRemove.setBudgetGroup(null);
//        budgetGroupService.saveBudgetGroup(budgetGroup);

//    }

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
