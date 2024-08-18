package com.patrykdankowski.financeflock.user.adapter;

import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.exception.BudgetGroupValidationException;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.port.UserMembershipDomainPort;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupValidatorPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class UserMembershipDomainAdapter implements UserMembershipDomainPort {

    private final BudgetGroupValidatorPort budgetGroupValidator;

    UserMembershipDomainAdapter(final BudgetGroupValidatorPort budgetGroupValidator) {
        this.budgetGroupValidator = budgetGroupValidator;

    }

    @Override
    public void leaveBudgetGroup(final UserDomainEntity loggedUser,
                                 final BudgetGroupDomainEntity budgetGroup,
                                 final boolean hasRole,
                                 final Long givenId) {

        log.info("Starting process of leave budget group for user with id {} ", loggedUser.getId());

        budgetGroupValidator.isMemberOfGivenGroup(loggedUser, budgetGroup, givenId);

        if (hasRole) {
            loggedUser.manageGroupMembership(null, Role.USER);
            budgetGroup.removeUser(loggedUser.getId());
            log.info("User with id {} left budget group", loggedUser.getId());
        } else {
            throw new BudgetGroupValidationException("Cannot leave budget group as admin");
        }


    }


    @Override
    public boolean toggleShareData(final UserDomainEntity loggedUser) {

        log.info("Starting process of toggle share data");

        loggedUser.toggleShareData();


        log.info("Toggled share data for user with id {} ", loggedUser.getId());

        return loggedUser.isShareData();
    }

}
