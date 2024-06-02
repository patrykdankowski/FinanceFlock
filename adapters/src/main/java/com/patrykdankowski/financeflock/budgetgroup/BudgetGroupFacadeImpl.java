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

        log.info("Before creating");

        final BudgetGroupDomainEntity budgetGroupDomainEntity =
                budgetGroupManagementDomain.createBudgetGroup(budgetGroupRequest, userFromContext);
        log.info("Before saving {}", budgetGroupDomainEntity);

        Long id = budgetGroupCommandService.saveBudgetGroup(budgetGroupDomainEntity).getId();
        log.info("after saving group");
        userCommandService.saveUser(userFromContext);
        log.info("Successfully finished process of create budget group");

        return id;

    }

    @Transactional
    @Override
//    @CacheEvict(cacheNames = "userEmailCache", allEntries = true)
    public void closeBudgetGroup(Long id) {

        log.info("Starting process of close group");

        UserDomainEntity userFromContext = authenticationService.getUserFromContext();
        Long userGroup = userFromContext.getBudgetGroupId();
        BudgetGroupDomainEntity budgetGroupDomainEntity = budgetGroupCommandService.findBudgetGroupById(userGroup);
        //TODO getListOfMembersId().stream().toList()) - czy warto do osobnej metody
        List<UserDomainEntity> listOfUsers = userCommandService.listOfUsersFromIds(budgetGroupDomainEntity.getListOfMembersId().stream().toList());
        budgetGroupManagementDomain.closeBudgetGroup(userFromContext, id, listOfUsers);

        userCommandService.saveAllUsers(listOfUsers);
        budgetGroupCommandService.deleteBudgetGroup(budgetGroupDomainEntity);

        log.info("Finished process of close group");
    }

    @Transactional
    @Override
    public void addUserToGroup(final String email, final Long id) {

        log.info("Starting process to add user to group");
        UserDomainEntity userFromContext = authenticationService.getUserFromContext();
        UserDomainEntity userToAdd = userCommandService.findUserByEmail(email);
        BudgetGroupDomainEntity budgetGroupDomainEntity = budgetGroupCommandService.findBudgetGroupById(userFromContext.getBudgetGroupId());
//        User userToAdd = commandRepository.findByEmail(email).get();
        budgetGroupMembershipDomain.addUserToGroup(userFromContext, userToAdd, id, budgetGroupDomainEntity);


        budgetGroupCommandService.saveBudgetGroup(budgetGroupDomainEntity);
        userCommandService.saveUser(userToAdd);

        log.info("Successfully finished process to add user to group");

    }

    @Transactional
    @Override
    public void removeUserFromGroup(final String email, final Long id) {

        log.info("Starting process to remove user from group");

        UserDomainEntity userFromContext = authenticationService.getUserFromContext();
        Long budgetGroupDomainEntityId = userFromContext.getBudgetGroupId();
        BudgetGroupDomainEntity budgetGroupDomainEntity =

                budgetGroupCommandService.findBudgetGroupById(budgetGroupDomainEntityId);
        UserDomainEntity userToRemove = userCommandService.findUserByEmail(email);

        budgetGroupMembershipDomain.removeUserFromGroup(userFromContext,
                userToRemove,
                budgetGroupDomainEntity,
                id);

        budgetGroupCommandService.saveBudgetGroup(budgetGroupDomainEntity);
        userCommandService.saveUser(userToRemove);

        log.info("Successfully finished process to remove user from group");

    }


}
