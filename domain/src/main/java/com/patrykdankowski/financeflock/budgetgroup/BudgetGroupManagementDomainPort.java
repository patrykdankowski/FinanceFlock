package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.UserDomainEntity;

import java.util.List;

public interface BudgetGroupManagementDomainPort {


    BudgetGroupDomainEntity createBudgetGroup(BudgetGroupRequest budgetGroupRequest,
                                              final UserDomainEntity userFromContext);

    void closeBudgetGroup(final UserDomainEntity userFromContext, final Long id, final List<UserDomainEntity> listOfUsers);


}
