package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.auth.AuthenticationService;
import com.patrykdankowski.financeflock.budgetgroup.dto.BudgetGroupRequest;
import com.patrykdankowski.financeflock.user.User;
import com.patrykdankowski.financeflock.user.UserCommandService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BudgetGroupFacadeImpl implements BudgetGroupFacade {
    private final BudgetGroupMembershipDomain budgetGroupMembershipDomain;
    private final BudgetGroupManagementDomain budgetGroupManagementDomain;
    private final UserCommandService userCommandService;
    private final BudgetGroupCommandService budgetGroupCommandService;
    private final AuthenticationService authenticationService;

    BudgetGroupFacadeImpl(final BudgetGroupMembershipDomain budgetGroupMembershipDomain,
                          final UserCommandService userCommandService,
                          final BudgetGroupCommandService budgetGroupCommandService,
                          final AuthenticationService authenticationService,
                          final BudgetGroupManagementDomain budgetGroupManagementDomain) {
        this.budgetGroupMembershipDomain = budgetGroupMembershipDomain;
        this.authenticationService = authenticationService;
        this.userCommandService = userCommandService;
        this.budgetGroupCommandService = budgetGroupCommandService;
        this.budgetGroupManagementDomain = budgetGroupManagementDomain;
    }

    @Transactional
    @Override
//    @CacheEvict(cacheNames = "userEmailCache", allEntries = true)
    public void createBudgetGroup(BudgetGroupRequest budgetGroupRequest) {

        log.info("Starting process of create budget group");

        final User userFromContext = authenticationService.getUserFromContext();


        final BudgetGroup budgetGroup = budgetGroupManagementDomain.createBudgetGroup
                (budgetGroupRequest, userFromContext);
        budgetGroupCommandService.saveBudgetGroup(budgetGroup);

        log.info("Successfully finished process of create budget group");

    }

    @Transactional
    @Override
//    @CacheEvict(cacheNames = "userEmailCache", allEntries = true)
    public void closeBudgetGroup() {

        log.info("Starting process of close group");

        User userFromContext = authenticationService.getUserFromContext();
        BudgetGroup userGroup = userFromContext.getBudgetGroup();
        final List<Long> listOfUserId =
                budgetGroupManagementDomain.closeBudgetGroup(userFromContext);

        userCommandService.saveAllUsers(userCommandService.listOfUsersFromIds(listOfUserId));
        budgetGroupCommandService.deleteBudgetGroup(userGroup);

        log.info("Finished process of close group");
    }

    @Transactional
    @Override
    public void addUserToGroup(String email) {

        log.info("Starting process to add user to group");

        User userFromContext = authenticationService.getUserFromContext();
        User userToAdd = userCommandService.findUserByEmail(email);

        budgetGroupMembershipDomain.addUserToGroup(userFromContext, userToAdd);


        budgetGroupCommandService.saveBudgetGroup(userFromContext.getBudgetGroup());

        log.info("Successfully finished process to add user to group");

    }

    @Transactional
    @Override
    public void removeUserFromGroup(String email) {

        log.info("Starting process to remove user from group");

        User userFromContext = authenticationService.getUserFromContext();
        BudgetGroup userGroup = userFromContext.getBudgetGroup();
        User userToRemove = userCommandService.findUserByEmail(email);

        budgetGroupMembershipDomain.removeUserFromGroup(userFromContext, userToRemove);

        budgetGroupCommandService.saveBudgetGroup(userGroup);
        userCommandService.saveUser(userToRemove);

        log.info("Successfully finished process to remove user from group");

    }


}
