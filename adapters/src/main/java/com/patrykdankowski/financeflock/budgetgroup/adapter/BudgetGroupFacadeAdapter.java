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
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.port.UserCommandServicePort;
import com.patrykdankowski.financeflock.user.port.UserValidatorPort;
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
    private final UserValidatorPort userValidator;
    private final BudgetGroupValidatorAdapter budgetGroupValidator;

    BudgetGroupFacadeAdapter(final BudgetGroupMembershipDomainPort budgetGroupMembershipDomain,
                             final UserCommandServicePort userCommandService,
                             final BudgetGroupCommandServicePort budgetGroupCommandService,
                             final AuthenticationServicePort authenticationService,
                             final BudgetGroupManagementDomainPort budgetGroupManagementDomain,
                             final UserValidatorPort userValidator,
                             final BudgetGroupValidatorAdapter budgetGroupValidator) {
        this.budgetGroupMembershipDomain = budgetGroupMembershipDomain;
        this.authenticationService = authenticationService;
        this.userCommandService = userCommandService;
        this.budgetGroupCommandService = budgetGroupCommandService;
        this.budgetGroupManagementDomain = budgetGroupManagementDomain;
        this.userValidator = userValidator;
        this.budgetGroupValidator = budgetGroupValidator;
    }

    @Transactional
    @Override
//    @CacheEvict(cacheNames = "userEmailCache", allEntries = true)
    public Long createBudgetGroup(BudgetGroupDto budgetGroupDto) {

        log.info("Starting process of create budget group");

        final UserDomainEntity loggedUser = authenticationService.getUserFromContext();
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

        UserDomainEntity loggedUser = authenticationService.getUserFromContext();
//        BudgetGroupDomainEntity budgetGroupById = retrieveBudgetGroupFromUser(loggedUser);
        BudgetGroupDomainEntity budgetGroupById = budgetGroupCommandService.findBudgetGroupById(id);

        budgetGroupValidator.validateIfUserIsAdmin(loggedUser, id, budgetGroupById);

        //TODO getListOfMembersId().stream().toList()) - czy warto do osobnej metody
        List<UserDomainEntity> listOfUsers = getUsersFromGroup(budgetGroupById);

        List<UserDomainEntity> updatedUsers = budgetGroupManagementDomain.closeBudgetGroup(listOfUsers);

        saveEntities(budgetGroupById, updatedUsers);

        log.info("Successfully finished process of closing group with ID: {}", id);


    }

//    private void validateIfUserIsAdmin(final UserDomainEntity loggedUser,
//                                       final Long id, final BudgetGroupDomainEntity budgetGroup) {
//
//        budgetGroupValidator.validateGroupForPotentialOwner(loggedUser, id, budgetGroup);
//        userValidator.hasGivenRole(loggedUser, Role.GROUP_ADMIN);
//    }

    private void saveEntities(final BudgetGroupDomainEntity budgetGroupFromLoggedUser, final List<UserDomainEntity> mappedEntities) {
        budgetGroupCommandService.deleteBudgetGroup(budgetGroupFromLoggedUser);
        userCommandService.saveAllUsers(mappedEntities);
    }

    private List<UserDomainEntity> getUsersFromGroup(final BudgetGroupDomainEntity budgetGroupFromLoggedUser) {
        return userCommandService.listOfUsersFromIds(budgetGroupFromLoggedUser.getListOfMembersId().stream().toList());
    }

//    private BudgetGroupDomainEntity retrieveBudgetGroupFromUser(final UserDomainEntity loggedUser) {
//        Long groupIdFromLoggedUser = loggedUser.getBudgetGroupId();
//
//        BudgetGroupDomainEntity budgetGroupFromLoggedUser = budgetGroupCommandService.findBudgetGroupById(groupIdFromLoggedUser);
//        return budgetGroupFromLoggedUser;
//    }

    @Transactional
    @Override
    public void addUserToGroup(final EmailDto email,
                               final Long id) {

        log.info("Starting process to add user to group");
        UserDomainEntity loggedUser = authenticationService.getUserFromContext();
        UserDomainEntity userToAdd = getUserFromEmail(email);

        if (loggedUser.getId().equals(userToAdd.getId())) {
            throw new SelfManagementInGroupException("You cannot add yourself to the group where you are already an admin.");
        }
//        BudgetGroupDomainEntity budgetGroupById = retrieveBudgetGroupFromUser(loggedUser);
        BudgetGroupDomainEntity budgetGroupById = budgetGroupCommandService.findBudgetGroupById(id);

        budgetGroupValidator.validateIfUserIsAdmin(loggedUser, id, budgetGroupById);


        budgetGroupMembershipDomain.addUserToGroup(userToAdd, budgetGroupById);

        saveGroupAndUser(budgetGroupById, userToAdd);

    }
//    @Transactional
//    @Override
//    public void addUserToGroup(final EmailDto email,
//                               final Long id) {
//
//        log.info("Starting process to add user to group");
//        UserDomainEntity loggedUser = authenticationService.getUserFromContext();
//
////        BudgetGroupDomainEntity budgetGroupById = retrieveBudgetGroupFromUser(loggedUser);
//        BudgetGroupDomainEntity budgetGroupById = budgetGroupCommandService.findBudgetGroupById(id);
//
//        budgetGroupValidator.validateIfUserIsAdmin(loggedUser, id, budgetGroupById);
//
//        UserDomainEntity userToAdd = getUserFromEmail(email);
//
//
//        boolean isAbleToJoinGroup = budgetGroupValidator.isNotMemberOfAnyGroup(userToAdd);
//
//        if (isAbleToJoinGroup) {
//            budgetGroupMembershipDomain.addUserToGroup(userToAdd, budgetGroupById);
//
//            saveGroupAndUser(budgetGroupById, userToAdd);
//
//        } else {
//            log.warn("User is not able to join budget group");
//            throw new BudgetGroupValidationException("Cannot add user to budget group" +
//                    " because user is already member of some group");
//        }
//
//    }

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
    public void removeUserFromGroup(final EmailDto email, final Long id) {

        log.info("Starting process to remove user from group");

        UserDomainEntity loggedUser = authenticationService.getUserFromContext();
        UserDomainEntity userToRemove = getUserFromEmail(email);

        if (loggedUser.getId().equals(userToRemove.getId())) {
            throw new SelfManagementInGroupException("You cannot remove yourself from the group. To leave the group, you need to close the group first.");
        }

        BudgetGroupDomainEntity budgetGroupById = budgetGroupCommandService.findBudgetGroupById(id);

        budgetGroupValidator.validateIfUserIsAdmin(loggedUser, id, budgetGroupById);

        boolean canBeRemovedFromGroup = budgetGroupValidator.isMemberOfGivenGroup(userToRemove, budgetGroupById);

        if (canBeRemovedFromGroup) {
            budgetGroupMembershipDomain.removeUserFromGroup(loggedUser,
                    userToRemove,
                    budgetGroupById,
                    id);

            saveGroupAndUser(budgetGroupById, userToRemove);

            log.info("Successfully removed user with email {} from group with ID: {}", email.getEmail(), id);
        } else {
            log.warn("User cannot be remove from budget group");
            throw new BudgetGroupValidationException("Cannot remove user from group" +
                    " because this user is not a member of your group");
        }


    }

//    private boolean isNotMemberOfAnyGroup(final UserDomainEntity loggedUser) {
//
//        boolean hasRole = userValidator.hasGivenRole(loggedUser, Role.USER);
//        boolean GroupIsNull = userValidator.groupIsNull(loggedUser);
//        return hasRole && GroupIsNull;
//    }

//
//    private boolean isMemberOfGivenGroup(final UserDomainEntity user,
//                                         final BudgetGroupDomainEntity budgetGroup) {
//        boolean hasRole = userValidator.hasGivenRole(user, Role.GROUP_MEMBER);
//        boolean groupIsNullNotNull = !userValidator.groupIsNull(user);
//        boolean isMemberOfGroup = budgetGroupValidator.isMember(user, budgetGroup, user.getBudgetGroupId());
//        return hasRole && groupIsNullNotNull && isMemberOfGroup;
//    }

}
