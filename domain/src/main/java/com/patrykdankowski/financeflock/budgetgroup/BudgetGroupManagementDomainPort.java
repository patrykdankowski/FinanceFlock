package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.User;

import java.util.List;

interface BudgetGroupManagementDomainPort {


    BudgetGroup createBudgetGroup(BudgetGroupRequest budgetGroupRequest,
                                  final User userFromContext);

    List<Long> closeBudgetGroup(final User userFromContext, final Long id);



}
