package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.UserDomainEntity;

import java.util.List;

public interface BudgetGroupManagementDomainPort {


    BudgetGroupDomainEntity createBudgetGroup(BudgetGroupRequest budgetGroupRequest,
                                              final UserDomainEntity userFromContext);

    List<Long> closeBudgetGroup(final UserDomainEntity userFromContext, final Long id);



}
