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

        final UserDomainEntity loggedUser = authenticationService.getUserFromContext();

        BudgetGroupDomainEntity createdDomainGroup =
                budgetGroupManagementDomain.createBudgetGroup(budgetGroupRequest, loggedUser);

        BudgetGroupDomainEntity savedDomainGroup = budgetGroupCommandService.saveBudgetGroup(createdDomainGroup);

        //this setter must be used here because before saving group, groupId in User is null
        loggedUser.setBudgetGroupId(savedDomainGroup.getId());

        UserDomainEntity savedLoggedUser = userCommandService.saveUser(loggedUser);

        long groupId = savedLoggedUser.getBudgetGroupId();

        log.info("Successfully finished process of create budget group");

        return groupId;

    }

    @Transactional
    @Override
//    @CacheEvict(cacheNames = "userEmailCache", allEntries = true)
    public void closeBudgetGroup(Long id) {

        log.info("Starting process of close group");

        UserDomainEntity loggedUser = authenticationService.getUserFromContext();

        Long groupIdFromLoggedUser = loggedUser.getBudgetGroupId();

        BudgetGroupDomainEntity budgetGroupFromLoggedUser = budgetGroupCommandService.findBudgetGroupById(groupIdFromLoggedUser);

        //TODO getListOfMembersId().stream().toList()) - czy warto do osobnej metody
        List<UserDomainEntity> listOfUsers = userCommandService.listOfUsersFromIds(budgetGroupFromLoggedUser.getListOfMembersId().stream().toList());

        List<UserDomainEntity> mappedEntities = budgetGroupManagementDomain.closeBudgetGroup(loggedUser, id, listOfUsers, budgetGroupFromLoggedUser);

        budgetGroupCommandService.deleteBudgetGroup(budgetGroupFromLoggedUser);
        userCommandService.saveAllUsers(mappedEntities);

        log.info("Finished process of close group");
    }

    @Transactional
    @Override
    public void addUserToGroup(final String email, final Long id) {

        log.info("Starting process to add user to group");
        UserDomainEntity loggedUser = authenticationService.getUserFromContext();

        UserDomainEntity userToAdd = userCommandService.findUserByEmail(email);

        BudgetGroupDomainEntity budgetGroupFromLoggedUser = budgetGroupCommandService.findBudgetGroupById(loggedUser.getBudgetGroupId());

        budgetGroupMembershipDomain.addUserToGroup(loggedUser, userToAdd, id, budgetGroupFromLoggedUser);

        budgetGroupCommandService.saveBudgetGroup(budgetGroupFromLoggedUser);
        userCommandService.saveUser(userToAdd);

        log.info("Successfully finished process to add user to group");

    }

    @Transactional
    @Override
    public void removeUserFromGroup(final String email, final Long id) {

        log.info("Starting process to remove user from group");

        UserDomainEntity loggedUser = authenticationService.getUserFromContext();

        Long groupIdFromLoggedUser = loggedUser.getBudgetGroupId();

        BudgetGroupDomainEntity budgetGroupFromLoggedUser =
                budgetGroupCommandService.findBudgetGroupById(groupIdFromLoggedUser);

        UserDomainEntity userToRemove = userCommandService.findUserByEmail(email);

        budgetGroupMembershipDomain.removeUserFromGroup(loggedUser,
                userToRemove,
                budgetGroupFromLoggedUser,
                id);

        budgetGroupCommandService.saveBudgetGroup(budgetGroupFromLoggedUser);
        userCommandService.saveUser(userToRemove);

        log.info("Successfully finished process to remove user from group");

    }


}
