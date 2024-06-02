package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.UserDomainEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
class BudgetGroupManagementDomainAdapter implements BudgetGroupManagementDomainPort {


    private final BudgetGroupFactory budgetGroupFactory;
    private final CommonDomainServicePort commonDomainService;


    BudgetGroupManagementDomainAdapter(final BudgetGroupFactory budgetGroupFactory,
                                       final CommonDomainServicePort commonDomainService) {
        this.budgetGroupFactory = budgetGroupFactory;
        this.commonDomainService = commonDomainService;
    }

    @Override
    public BudgetGroupDomainEntity createBudgetGroup(final BudgetGroupRequest budgetGroupRequest,
                                                     final UserDomainEntity userFromContext) {
        isUserAbleToCreateBudgetGroup(userFromContext);

        BudgetGroupDomainEntity budgetGroupDomainEntity =
                budgetGroupFactory.createBudgetGroupFromRequest(userFromContext, budgetGroupRequest);
        assignRoleAndBudgetGroupForUser(userFromContext, budgetGroupDomainEntity.getId(), Role.GROUP_ADMIN);
        return budgetGroupDomainEntity;
    }

    private void isUserAbleToCreateBudgetGroup(final UserDomainEntity userFromContext) {
        if (userFromContext.getRole() != Role.USER || userFromContext.getBudgetGroupId() != null) {
            //TODO CUSTOMOWY EXCEPTION
            throw new IllegalStateException("Cannot create budget group");
        }
    }

    @Override
    public void closeBudgetGroup(final UserDomainEntity userFromContext, final Long groupId, final List<UserDomainEntity> listOfUsers) {
        //TODO zaimplementować metode informujaca all userów z grupy ze grupa została zamknięta

         commonDomainService.
                 checkIfGroupExists(userFromContext, groupId);

        validateOwnership(groupId, userFromContext);

        resetUsersRolesAndDetachFromGroup(listOfUsers);

    }

    private void validateOwnership(final Long budgetGroupIdToValidate,
                                   final UserDomainEntity userFromContext) {
        if (!budgetGroupIdToValidate.equals(userFromContext.getBudgetGroupId()) || !userFromContext.getRole().equals(Role.GROUP_ADMIN)) {
            log.error("User {} is not a owner of a group", userFromContext.getName());
            throw new IllegalStateException("Only the group owner can close the group");
        }
    }

    private void resetUsersRolesAndDetachFromGroup(final List<UserDomainEntity> listOfUsers) {
        listOfUsers.stream().map(
                userToMap ->
                {
                    assignRoleAndBudgetGroupForUser(userToMap, null, Role.USER);
                    return userToMap.getId();
                });
        log.info("Detached users from group");

    }

    private void assignRoleAndBudgetGroupForUser(final UserDomainEntity userFromContext,
                                                 final Long budgetGroupDomainEntity,
                                                 final Role role) {
        userFromContext.setRole(role);
        userFromContext.setBudgetGroupId(budgetGroupDomainEntity);
    }
}
