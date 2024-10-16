package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.budgetgroup.exception.BudgetGroupValidationException;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupMembershipDomainPort;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupValidatorPort;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
class BudgetGroupMembershipDomainAdapter implements BudgetGroupMembershipDomainPort {


    private final BudgetGroupValidatorPort budgetGroupValidator;

    private BudgetGroupMembershipDomainAdapter(final BudgetGroupValidatorPort budgetGroupValidator) {
        this.budgetGroupValidator = budgetGroupValidator;
    }

    @Override
    public void addUserToGroup(
            final UserDomainEntity userToAdd,
            final BudgetGroupDomainEntity budgetGroup) {


        boolean isAbleToJoinGroup = budgetGroupValidator.isNotMemberOfAnyGroup(userToAdd);

        if (isAbleToJoinGroup) {

            budgetGroupValidator.validateSizeOfGroup(budgetGroup);

            budgetGroup.addUser(userToAdd.getId());
            userToAdd.manageGroupMembership(budgetGroup.getId(), Role.GROUP_MEMBER);

        } else {
            log.warn("User is not able to join budget group");
            throw new BudgetGroupValidationException("Cannot add user to budget group" +
                    " because user is already member of some group");
        }


    }


    @Override
    public void removeUserFromGroup(final UserDomainEntity userToRemove,
                                    final BudgetGroupDomainEntity budgetGroup,
                                    final Long givenGroupId) {


        final boolean isAbleToRemove = budgetGroupValidator.isMemberOfGivenIdGroup(userToRemove, budgetGroup, givenGroupId);

        if (isAbleToRemove) {

            userToRemove.manageGroupMembership(null, Role.USER);
            budgetGroup.removeUser(userToRemove.getId());
        } else {
            log.warn("User cannot be remove from budget group");
            throw new BudgetGroupValidationException("Cannot remove user from group" +
                    " because this user is not a member of your group");
        }

    }
}
