package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.budgetgroup.exception.BudgetGroupValidationException;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupFactoryPort;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.model.record.BudgetGroupDescription;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupManagementDomainPort;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupValidatorPort;
import com.patrykdankowski.financeflock.user.port.UserValidatorPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
class BudgetGroupManagementDomainAdapter implements BudgetGroupManagementDomainPort {


    private final BudgetGroupFactoryPort budgetGroupFactory;
    private final UserValidatorPort userValidator;
    private final BudgetGroupValidatorPort budgetGroupValidator;


    BudgetGroupManagementDomainAdapter(final BudgetGroupFactoryPort budgetGroupFactory,
                                       final UserValidatorPort userValidator,
                                       final BudgetGroupValidatorPort budgetGroupValidator) {
        this.budgetGroupFactory = budgetGroupFactory;

        this.userValidator = userValidator;
        this.budgetGroupValidator = budgetGroupValidator;
    }

    @Override
    public BudgetGroupDomainEntity createBudgetGroup(final BudgetGroupDescription budgetGroupDescription,
                                                     final UserDomainEntity userFromContext) {
        boolean isAbleToCreateGroup = isUserAbleToCreateBudgetGroup(userFromContext);

        if (isAbleToCreateGroup) {
            return budgetGroupFactory.createBudgetGroupFromRequest(userFromContext, budgetGroupDescription);
        } else {
            log.warn("User is not able to create budget group");
            throw new BudgetGroupValidationException("Cannot create budget group, probably u are a member of different group. " +
                    "Leave your current group and try again");
        }


    }

    private boolean isUserAbleToCreateBudgetGroup(final UserDomainEntity loggedUser) {

        boolean hasRole = userValidator.hasGivenRole(loggedUser, Role.USER);
        boolean GroupIsNull = userValidator.groupIsNull(loggedUser);
        return hasRole && GroupIsNull;
    }

    @Override
    public List<UserDomainEntity> closeBudgetGroup(final UserDomainEntity potentialOwner,
                                                   final Long groupId,
                                                   final List<UserDomainEntity> listOfUsers, BudgetGroupDomainEntity budgetGroupDomainEntity) {
        //TODO zaimplementować metode informujaca all userów z grupy ze grupa została zamknięta

        budgetGroupValidator.validateGroupForPotentialOwner(potentialOwner, groupId, budgetGroupDomainEntity);

        List<UserDomainEntity> mapppedEntities = resetUsersRolesAndDetachFromGroup(listOfUsers);

        return mapppedEntities;

    }


    private List<UserDomainEntity> resetUsersRolesAndDetachFromGroup(final List<UserDomainEntity> listOfUsers) {
        return listOfUsers.stream().map(
                userToMap ->
                {
                    userToMap.manageGroupMembership(null, Role.USER);
                    return userToMap;
                }

        ).collect(Collectors.toList());
    }


}
