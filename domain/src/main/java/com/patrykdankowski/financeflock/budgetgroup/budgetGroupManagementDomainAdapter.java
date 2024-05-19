package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
class budgetGroupManagementDomainAdapter implements BudgetGroupManagementDomainPort {


    private final BudgetGroupFactory budgetGroupFactory;
    private final CommonDomainServicePort commonDomainService;


    budgetGroupManagementDomainAdapter(final BudgetGroupFactory budgetGroupFactory,
                                       final CommonDomainServicePort commonDomainService) {
        this.budgetGroupFactory = budgetGroupFactory;
        this.commonDomainService = commonDomainService;
    }

    @Override
    public BudgetGroup createBudgetGroup(final BudgetGroupRequest budgetGroupRequest,
                                         final User userFromContext) {

        isUserAbleToCreateBudgetGroup(userFromContext);

        BudgetGroup budgetGroup = budgetGroupFactory.createBudgetGroupFromRequest(userFromContext, budgetGroupRequest);
        assignRoleAndBudgetGroupForUser(userFromContext, budgetGroup, Role.GROUP_ADMIN);
        return budgetGroup;
    }

    private void isUserAbleToCreateBudgetGroup(final User userFromContext) {
        if (userFromContext.getRole() != Role.USER || userFromContext.getBudgetGroup() != null) {
            //TODO CUSTOMOWY EXCEPTION
            throw new IllegalStateException("Cannot create budget group");
        }
    }

    @Override
    public List<Long> closeBudgetGroup(final User userFromContext,final Long id) {
        //TODO zaimplementować metode informujaca all userów z grupy ze grupa została zamknięta

        final BudgetGroup budgetGroup = commonDomainService.
                validateAndGetGroup(userFromContext,id);

        validateOwnership(budgetGroup, userFromContext);

        return resetUsersRolesAndDetachFromGroup(budgetGroup);

    }

    private void validateOwnership(final BudgetGroup budgetGroup,
                                   final User userFromContext) {
        if (!budgetGroup.getOwner().equals(userFromContext)) {
            log.error("User {} is not a owner of a group", userFromContext.getName());
            throw new IllegalStateException("Only the group owner can close the group");
        }
    }

    private List<Long> resetUsersRolesAndDetachFromGroup(final BudgetGroup budgetGroup) {
        List<Long> listOfUsersId = budgetGroup.getListOfMembers().stream().map(
                userToMap ->
                {
                    assignRoleAndBudgetGroupForUser(userToMap, null, Role.USER);
                    return userToMap.getId();
                }).toList();
        log.info("Detached users from group");
        return listOfUsersId;
    }

    private void assignRoleAndBudgetGroupForUser(final User userFromContext,
                                                 final BudgetGroup budgetGroup,
                                                 final Role role) {
        userFromContext.setRole(role);
        userFromContext.setBudgetGroup(budgetGroup);
    }
}
