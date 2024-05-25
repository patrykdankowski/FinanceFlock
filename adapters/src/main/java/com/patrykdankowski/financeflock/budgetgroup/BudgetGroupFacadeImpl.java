package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.auth.AuthenticationServicePort;
import com.patrykdankowski.financeflock.user.UserDomainEntity;
import com.patrykdankowski.financeflock.user.UserCommandServicePort;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BudgetGroupFacadeImpl implements BudgetGroupFacade {
    private final BudgetGroupMembershipDomainPort budgetGroupMembershipDomain;
    private final BudgetGroupManagementDomainPort budgetGroupManagementDomain;
    private final UserCommandServicePort userCommandService;
    private final BudgetGroupCommandServicePort budgetGroupCommandService;
    private final AuthenticationServicePort authenticationService;

    BudgetGroupFacadeImpl(final BudgetGroupMembershipDomainPort budgetGroupMembershipDomain,
                          final UserCommandServicePort userCommandService,
                          final BudgetGroupCommandServicePort budgetGroupCommandService,
                          final AuthenticationServicePort authenticationService,
                          final BudgetGroupManagementDomainPort budgetGroupManagementDomain) {
        this.budgetGroupMembershipDomain = budgetGroupMembershipDomain;
        this.authenticationService = authenticationService;
        this.userCommandService = userCommandService;
        this.budgetGroupCommandService = budgetGroupCommandService;
        this.budgetGroupManagementDomain = budgetGroupManagementDomain;
    }

    @Transactional
    @Override
//    @CacheEvict(cacheNames = "userEmailCache", allEntries = true)
    public Long createBudgetGroup(BudgetGroupRequest budgetGroupRequest) {

        log.info("Starting process of create budget group");

        final UserDomainEntity userFromContext = authenticationService.getUserFromContext();


        final BudgetGroupDomainEntity budgetGroupDomainEntity = budgetGroupManagementDomain.createBudgetGroup
                (budgetGroupRequest, userFromContext);
        log.info("before");
        Long id = budgetGroupCommandService.saveBudgetGroup(budgetGroupDomainEntity).getId();
        log.info("after");

        log.info("Successfully finished process of create budget group");

        return id;

    }

    @Transactional
    @Override
//    @CacheEvict(cacheNames = "userEmailCache", allEntries = true)
    public void closeBudgetGroup(Long id) {

        log.info("Starting process of close group");

        UserDomainEntity userFromContext = authenticationService.getUserFromContext();
        BudgetGroupDomainEntity userGroup = userFromContext.getBudgetGroup();
        final List<Long> listOfUserId =
                budgetGroupManagementDomain.closeBudgetGroup(userFromContext, id);

        userCommandService.saveAllUsers(userCommandService.listOfUsersFromIds(listOfUserId));
        budgetGroupCommandService.deleteBudgetGroup(userGroup);

        log.info("Finished process of close group");
    }

    @Transactional
    @Override
    public void addUserToGroup(final String email, final Long id) {

        log.info("Starting process to add user to group");
        UserDomainEntity userFromContext = authenticationService.getUserFromContext();
        UserDomainEntity userToAdd = userCommandService.findUserByEmail(email);
//        User userToAdd = commandRepository.findByEmail(email).get();
        budgetGroupMembershipDomain.addUserToGroup(userFromContext, userToAdd, id);


        budgetGroupCommandService.saveBudgetGroup(userFromContext.getBudgetGroup());

        log.info("Successfully finished process to add user to group");

    }

    @Transactional
    @Override
    public void removeUserFromGroup(final String email, final Long id) {

        log.info("Starting process to remove user from group");

        UserDomainEntity userFromContext = authenticationService.getUserFromContext();
        BudgetGroupDomainEntity userGroup = userFromContext.getBudgetGroup();
        UserDomainEntity userToRemove = userCommandService.findUserByEmail(email);

        budgetGroupMembershipDomain.removeUserFromGroup(userFromContext, userToRemove, id);

        budgetGroupCommandService.saveBudgetGroup(userGroup);
        userCommandService.saveUser(userToRemove);

        log.info("Successfully finished process to remove user from group");

    }


}
