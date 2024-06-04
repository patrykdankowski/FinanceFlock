package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.UserDomainEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<UserDomainEntity> closeBudgetGroup(final UserDomainEntity userFromContext,
                                                   final Long groupId,
                                                   final List<UserDomainEntity> listOfUsers, BudgetGroupDomainEntity budgetGroupDomainEntity) {
        //TODO zaimplementować metode informujaca all userów z grupy ze grupa została zamknięta

        commonDomainService.validateGroupForPotentialOwner(userFromContext,groupId,budgetGroupDomainEntity);
//        validateGroupForPotentialOwner(userFromContext, groupId, budgetGroupDomainEntity);

        List<UserDomainEntity> mapppedEntities = resetUsersRolesAndDetachFromGroup(listOfUsers);

        return mapppedEntities;

    }

    private void validateGroupForPotentialOwner(UserDomainEntity userFromContext, Long groupId, BudgetGroupDomainEntity budgetGroupDomainEntity) {
        commonDomainService.checkIfGroupIsNotNull(userFromContext);
        commonDomainService.checkRoleForUser(userFromContext, Role.GROUP_ADMIN);
        commonDomainService.checkIfUserIsMemberOfGroup(userFromContext, budgetGroupDomainEntity);
        commonDomainService.checkIdGroupWithGivenId(groupId, userFromContext.getBudgetGroupId());
    }



    private List<UserDomainEntity> resetUsersRolesAndDetachFromGroup(final List<UserDomainEntity> listOfUsers) {
        return listOfUsers.stream().map(
                userToMap ->
                {
                    assignRoleAndBudgetGroupForUser(userToMap, null, Role.USER);
                    return userToMap;
                }

                ).collect(Collectors.toList());
//        log.info("Detached users from group");

    }

    private void assignRoleAndBudgetGroupForUser(final UserDomainEntity userFromContext,
                                                 final Long budgetGroupDomainEntity,
                                                 final Role role) {
        userFromContext.setRole(role);
        userFromContext.setBudgetGroupId(budgetGroupDomainEntity);
    }
}
