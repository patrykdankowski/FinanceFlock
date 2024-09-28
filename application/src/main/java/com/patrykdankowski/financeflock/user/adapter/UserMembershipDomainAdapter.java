package com.patrykdankowski.financeflock.user.adapter;

import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupValidatorPort;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.port.UserMembershipDomainPort;
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
                                 final Long givenId) {


        budgetGroupValidator.validateMembership(loggedUser, budgetGroup, givenId);

        loggedUser.manageGroupMembership(null, Role.USER);
        budgetGroup.removeUser(loggedUser.getId());

    }


    @Override
    public boolean toggleShareData(final UserDomainEntity loggedUser) {

        loggedUser.toggleShareData();

        return loggedUser.isShareData();
    }

}
