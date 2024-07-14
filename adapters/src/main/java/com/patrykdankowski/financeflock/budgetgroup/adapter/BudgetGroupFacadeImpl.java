package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.auth.port.AuthenticationServicePort;
import com.patrykdankowski.financeflock.budgetgroup.dto.BudgetGroupRequest;
import com.patrykdankowski.financeflock.budgetgroup.dto.EmailRequest;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.model.record.BudgetGroupDescription;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupCommandServicePort;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupFacade;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupManagementDomainPort;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupMembershipDomainPort;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.port.UserCommandServicePort;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
class BudgetGroupFacadeImpl implements BudgetGroupFacade {
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

        final BudgetGroupDescription budgetGroupDescription = new BudgetGroupDescription(budgetGroupRequest.getDescription());

        BudgetGroupDomainEntity createdDomainGroup =
                budgetGroupManagementDomain.createBudgetGroup(budgetGroupDescription, loggedUser);

        BudgetGroupDomainEntity savedDomainGroup = budgetGroupCommandService.saveBudgetGroup(createdDomainGroup);

        updateGroupMembership(loggedUser, savedDomainGroup);

        log.info("Successfully finished process of creating budget group with ID: {}", savedDomainGroup.getId());

        return savedDomainGroup.getId();

    }

    private void updateGroupMembership(final UserDomainEntity loggedUser, final BudgetGroupDomainEntity savedDomainGroup) {
        loggedUser.manageGroupMembership(savedDomainGroup.getId(), Role.GROUP_ADMIN);

        userCommandService.saveUser(loggedUser);
    }

    @Transactional
    @Override
//    @CacheEvict(cacheNames = "userEmailCache", allEntries = true)
    public void closeBudgetGroup(Long id) {

        log.info("Starting process of close group");

        UserDomainEntity loggedUser = authenticationService.getUserFromContext();

        BudgetGroupDomainEntity budgetGroupFromLoggedUser = retrieveBudgetGroupFromUser(loggedUser);

        //TODO getListOfMembersId().stream().toList()) - czy warto do osobnej metody
        List<UserDomainEntity> listOfUsers = getUsersFromGroup(budgetGroupFromLoggedUser);

        List<UserDomainEntity> updatedUsers = budgetGroupManagementDomain.closeBudgetGroup(loggedUser, id, listOfUsers, budgetGroupFromLoggedUser);

        deleteGroupAndSaveUsers(budgetGroupFromLoggedUser, updatedUsers);

        log.info("Successfully finished process of closing group with ID: {}", id);
    }

    private void deleteGroupAndSaveUsers(final BudgetGroupDomainEntity budgetGroupFromLoggedUser, final List<UserDomainEntity> mappedEntities) {
        budgetGroupCommandService.deleteBudgetGroup(budgetGroupFromLoggedUser);
        userCommandService.saveAllUsers(mappedEntities);
    }

    private List<UserDomainEntity> getUsersFromGroup(final BudgetGroupDomainEntity budgetGroupFromLoggedUser) {
        return userCommandService.listOfUsersFromIds(budgetGroupFromLoggedUser.getListOfMembersId().stream().toList());
    }

    private BudgetGroupDomainEntity retrieveBudgetGroupFromUser(final UserDomainEntity loggedUser) {
        Long groupIdFromLoggedUser = loggedUser.getBudgetGroupId();

        BudgetGroupDomainEntity budgetGroupFromLoggedUser = budgetGroupCommandService.findBudgetGroupById(groupIdFromLoggedUser);
        return budgetGroupFromLoggedUser;
    }

    @Transactional
    @Override
    public void addUserToGroup(final EmailRequest email, final Long id) {

        log.info("Starting process to add user to group");
        UserDomainEntity loggedUser = authenticationService.getUserFromContext();

        BudgetGroupDomainEntity budgetGroupFromLoggedUser = budgetGroupCommandService.findBudgetGroupById(loggedUser.getBudgetGroupId());

        UserDomainEntity userToAdd = getUserFromEmail(email);

        budgetGroupMembershipDomain.addUserToGroup(loggedUser, userToAdd, id, budgetGroupFromLoggedUser);

        saveGroupAndUser(budgetGroupFromLoggedUser, userToAdd);

        log.info("Successfully added user with email {} to group with ID: {}", email.getEmail(), id);

    }

    private void saveGroupAndUser(final BudgetGroupDomainEntity budgetGroupFromLoggedUser, final UserDomainEntity userToAdd) {
        budgetGroupCommandService.saveBudgetGroup(budgetGroupFromLoggedUser);
        userCommandService.saveUser(userToAdd);
    }

    private UserDomainEntity getUserFromEmail(final EmailRequest email) {
        final String userEmail = email.getEmail();

        UserDomainEntity userToAdd = userCommandService.findUserByEmail(userEmail);
        return userToAdd;
    }

    @Transactional
    @Override
    public void removeUserFromGroup(final EmailRequest email, final Long id) {

        log.info("Starting process to remove user from group");

        UserDomainEntity loggedUser = authenticationService.getUserFromContext();

        BudgetGroupDomainEntity budgetGroupFromLoggedUser = retrieveBudgetGroupFromUser(loggedUser);

        UserDomainEntity userToRemove = getUserFromEmail(email);

        budgetGroupMembershipDomain.removeUserFromGroup(loggedUser,
                userToRemove,
                budgetGroupFromLoggedUser,
                id);

        saveGroupAndUser(budgetGroupFromLoggedUser, userToRemove);

        log.info("Successfully removed user with email {} from group with ID: {}", email.getEmail(), id);

    }
}
