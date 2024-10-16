package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.auth.port.AuthenticationServicePort;
import com.patrykdankowski.financeflock.budgetgroup.dto.BudgetGroupDto;
import com.patrykdankowski.financeflock.budgetgroup.dto.EmailDto;
import com.patrykdankowski.financeflock.budgetgroup.exception.BudgetGroupValidationException;
import com.patrykdankowski.financeflock.budgetgroup.exception.SelfManagementInGroupException;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.model.record.BudgetGroupDescription;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupCommandServicePort;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupFacadePort;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupManagementDomainPort;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupMembershipDomainPort;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupValidatorPort;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.port.UserCommandServicePort;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
class BudgetGroupFacadeAdapter implements BudgetGroupFacadePort {
    private final BudgetGroupMembershipDomainPort budgetGroupMembershipDomain;
    private final BudgetGroupManagementDomainPort budgetGroupManagementDomain;
    private final UserCommandServicePort userCommandService;
    private final BudgetGroupCommandServicePort budgetGroupCommandService;
    private final AuthenticationServicePort authenticationService;
    private final BudgetGroupValidatorPort budgetGroupValidator;

    BudgetGroupFacadeAdapter(final BudgetGroupMembershipDomainPort budgetGroupMembershipDomain,
                             final UserCommandServicePort userCommandService,
                             final BudgetGroupCommandServicePort budgetGroupCommandService,
                             final AuthenticationServicePort authenticationService,
                             final BudgetGroupManagementDomainPort budgetGroupManagementDomain,
                             final BudgetGroupValidatorPort budgetGroupValidator) {
        this.budgetGroupMembershipDomain = budgetGroupMembershipDomain;
        this.authenticationService = authenticationService;
        this.userCommandService = userCommandService;
        this.budgetGroupCommandService = budgetGroupCommandService;
        this.budgetGroupManagementDomain = budgetGroupManagementDomain;
        this.budgetGroupValidator = budgetGroupValidator;
    }

    @Transactional
    @Override
//    @CacheEvict(cacheNames = "userEmailCache", allEntries = true)
    public Long createBudgetGroup(BudgetGroupDto budgetGroupDto) {

        log.info("Starting process of create budget group");

        final UserDomainEntity loggedUser = authenticationService.getFullUserFromContext();
        final BudgetGroupDescription budgetGroupDescription = new BudgetGroupDescription(budgetGroupDto.getDescription());

        boolean isAbleToCreateGroup = budgetGroupValidator.isNotMemberOfAnyGroup(loggedUser);

        if (isAbleToCreateGroup) {
            final BudgetGroupDomainEntity createdDomainGroup = budgetGroupManagementDomain.createBudgetGroup(budgetGroupDescription, loggedUser.getId());

            BudgetGroupDomainEntity savedDomainGroup = budgetGroupCommandService.saveBudgetGroup(createdDomainGroup);

            updateGroupMembership(loggedUser, savedDomainGroup);

            log.info("Successfully finished process of creating budget group with ID: {}", savedDomainGroup.getId());

            return savedDomainGroup.getId();

        } else {
            log.warn("User is not able to create budget group");
            throw new BudgetGroupValidationException("Cannot create budget group, probably u are a member of different group. " +
                    "Leave your current group and try again");
        }
    }

    private void updateGroupMembership(final UserDomainEntity loggedUser,
                                       final BudgetGroupDomainEntity savedDomainGroup) {
        loggedUser.manageGroupMembership(savedDomainGroup.getId(), Role.GROUP_ADMIN);

        userCommandService.saveUser(loggedUser);
    }

    @Transactional
    @Override
//    @CacheEvict(cacheNames = "userEmailCache", allEntries = true)
    public void closeBudgetGroup(Long id) {

        log.info("Starting process of close group");

        UserDomainEntity loggedUser = authenticationService.getFullUserFromContext();
        BudgetGroupDomainEntity budgetGroupById = budgetGroupCommandService.findBudgetGroupById(id);

        budgetGroupValidator.validateIfUserIsAdmin(loggedUser, id, budgetGroupById);

        //TODO getListOfMembersId().stream().toList()) - czy warto do osobnej metody
        List<UserDomainEntity> listOfUsers = getUsersFromGroup(budgetGroupById);

        List<UserDomainEntity> updatedUsers = budgetGroupManagementDomain.closeBudgetGroup(listOfUsers);

        saveEntities(budgetGroupById, updatedUsers);

        log.info("Successfully finished process of closing group with ID: {}", id);


    }

    private void saveEntities(final BudgetGroupDomainEntity budgetGroupFromLoggedUser, final List<UserDomainEntity> mappedEntities) {
        budgetGroupCommandService.deleteBudgetGroup(budgetGroupFromLoggedUser);
        userCommandService.saveAllUsers(mappedEntities);
    }

    private List<UserDomainEntity> getUsersFromGroup(final BudgetGroupDomainEntity budgetGroupFromLoggedUser) {
        return userCommandService.listOfUsersFromIds(budgetGroupFromLoggedUser.getListOfMembersId().stream().toList());
    }

    @Transactional
    @Override
    public void addUserToGroup(final EmailDto email,
                               final Long id) {

        log.info("Starting process to add user to group");
        UserDomainEntity loggedUser = authenticationService.getFullUserFromContext();
        UserDomainEntity userToAdd = getUserFromEmail(email);

        if (loggedUser.getId().equals(userToAdd.getId())) {
            throw new SelfManagementInGroupException("You cannot add yourself to the group where you are already an admin.");
        }
        BudgetGroupDomainEntity budgetGroupById = budgetGroupCommandService.findBudgetGroupById(id);

        budgetGroupValidator.validateIfUserIsAdmin(loggedUser, id, budgetGroupById);


        budgetGroupMembershipDomain.addUserToGroup(userToAdd, budgetGroupById);

        saveGroupAndUser(budgetGroupById, userToAdd);

    }

    private void saveGroupAndUser(final BudgetGroupDomainEntity budgetGroupFromLoggedUser, final UserDomainEntity userToAdd) {
        budgetGroupCommandService.saveBudgetGroup(budgetGroupFromLoggedUser);
        userCommandService.saveUser(userToAdd);
    }

    private UserDomainEntity getUserFromEmail(final EmailDto email) {
        final String userEmail = email.getEmail();

        UserDomainEntity userToAdd = userCommandService.findUserByEmail(userEmail);
        return userToAdd;
    }

    @Transactional
    @Override
    public void removeUserFromGroup(final EmailDto email, final Long groupId) {

        log.info("Starting process to remove user from group");

        UserDomainEntity loggedUser = authenticationService.getFullUserFromContext();
        UserDomainEntity userToRemove = getUserFromEmail(email);

        if (loggedUser.getId().equals(userToRemove.getId())) {
            throw new SelfManagementInGroupException("You cannot remove yourself from the group. To leave the group, you need to close the group first.");
        }

        BudgetGroupDomainEntity budgetGroupById = budgetGroupCommandService.findBudgetGroupById(groupId);

        budgetGroupValidator.validateIfUserIsAdmin(loggedUser, groupId, budgetGroupById);

        budgetGroupMembershipDomain.removeUserFromGroup(userToRemove, budgetGroupById, groupId);


        saveGroupAndUser(budgetGroupById, userToRemove);

        log.info("Successfully removed user with email {} from group with ID: {}", email.getEmail(), groupId);
    }
}

