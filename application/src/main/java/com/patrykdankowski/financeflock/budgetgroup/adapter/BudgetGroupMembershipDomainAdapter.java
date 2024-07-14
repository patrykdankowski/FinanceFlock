package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupMembershipDomainPort;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupValidatorPort;
import com.patrykdankowski.financeflock.user.port.UserValidatorPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
class BudgetGroupMembershipDomainAdapter implements BudgetGroupMembershipDomainPort {


    private final BudgetGroupValidatorPort budgetGroupValidator;
    private final UserValidatorPort userValidator;

    BudgetGroupMembershipDomainAdapter(final BudgetGroupValidatorPort budgetGroupValidator,
                                       final UserValidatorPort userValidator) {
        this.budgetGroupValidator = budgetGroupValidator;
        this.userValidator = userValidator;
    }

    @Override
    public void addUserToGroup(final UserDomainEntity potentialGroupOwner,
                               final UserDomainEntity userToAdd,
                               final Long givenGroupId,
                               final BudgetGroupDomainEntity budgetGroup) {

        budgetGroupValidator.validateGroupForPotentialOwner(potentialGroupOwner, givenGroupId, budgetGroup);
        userValidator.hasGivenRole(potentialGroupOwner, Role.GROUP_ADMIN);
        budgetGroupValidator.validateSizeOfGroup(budgetGroup);

        boolean hasRole = userValidator.hasGivenRole(userToAdd, Role.USER);
        boolean GroupIsNull = userValidator.groupIsNull(userToAdd);

        if (hasRole && GroupIsNull) {
            budgetGroup.addUser(userToAdd.getId());
            userToAdd.manageGroupMembership(budgetGroup.getId(), Role.GROUP_MEMBER);
        }

    }


    @Override
    public void removeUserFromGroup(final UserDomainEntity potentialOwner,
                                    final UserDomainEntity userToRemove,
                                    final BudgetGroupDomainEntity budgetGroup,
                                    final Long givenGroupId) {

        budgetGroupValidator.validateGroupForPotentialOwner(potentialOwner, givenGroupId, budgetGroup);
        userValidator.hasGivenRole(potentialOwner, Role.GROUP_ADMIN);


        boolean hasRole = userValidator.hasGivenRole(userToRemove, Role.GROUP_MEMBER);
        boolean GroupIsNull = userValidator.groupIsNull(userToRemove);

        if (hasRole && !GroupIsNull) {
            budgetGroup.removeUser(userToRemove.getId());
            userToRemove.manageGroupMembership(null, Role.USER);
        }


    }
}
