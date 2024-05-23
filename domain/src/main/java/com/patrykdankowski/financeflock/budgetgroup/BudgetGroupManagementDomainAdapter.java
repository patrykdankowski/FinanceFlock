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

        BudgetGroupDomainEntity budgetGroupDomainEntity = budgetGroupFactory.createBudgetGroupFromRequest(userFromContext, budgetGroupRequest);
        assignRoleAndBudgetGroupForUser(userFromContext, budgetGroupDomainEntity, Role.GROUP_ADMIN);
        return budgetGroupDomainEntity;
    }

    private void isUserAbleToCreateBudgetGroup(final UserDomainEntity userFromContext) {
        if (userFromContext.getRole() != Role.USER || userFromContext.getBudgetGroup() != null) {
            //TODO CUSTOMOWY EXCEPTION
            throw new IllegalStateException("Cannot create budget group");
        }
    }

    @Override
    public List<Long> closeBudgetGroup(final UserDomainEntity userFromContext, final Long id) {
        //TODO zaimplementować metode informujaca all userów z grupy ze grupa została zamknięta

        final BudgetGroupDomainEntity budgetGroupDomainEntity = commonDomainService.
                validateAndGetGroup(userFromContext,id);

        validateOwnership(budgetGroupDomainEntity, userFromContext);

        return resetUsersRolesAndDetachFromGroup(budgetGroupDomainEntity);

    }

    private void validateOwnership(final BudgetGroupDomainEntity budgetGroupDomainEntity,
                                   final UserDomainEntity userFromContext) {
        if (!budgetGroupDomainEntity.getOwner().equals(userFromContext)) {
            log.error("User {} is not a owner of a group", userFromContext.getName());
            throw new IllegalStateException("Only the group owner can close the group");
        }
    }

    private List<Long> resetUsersRolesAndDetachFromGroup(final BudgetGroupDomainEntity budgetGroupDomainEntity) {
        List<Long> listOfUsersId = budgetGroupDomainEntity.getListOfMembers().stream().map(
                userToMap ->
                {
                    assignRoleAndBudgetGroupForUser(userToMap, null, Role.USER);
                    return userToMap.getId();
                }).toList();
        log.info("Detached users from group");
        return listOfUsersId;
    }

    private void assignRoleAndBudgetGroupForUser(final UserDomainEntity userFromContext,
                                                 final BudgetGroupDomainEntity budgetGroupDomainEntity,
                                                 final Role role) {
        userFromContext.setRole(role);
        userFromContext.setBudgetGroup(budgetGroupDomainEntity);
    }
}
