package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.UserDomainEntity;

import java.util.HashSet;
import java.util.Set;

class BudgetGroupFactory {

    public BudgetGroupDomainEntity createBudgetGroupFromRequest(UserDomainEntity userFromContext, BudgetGroupRequest budgetGroupRequest) {

        var budgetGroup = new BudgetGroupDomainEntity(
                null, budgetGroupRequest.getDescription(), userFromContext.getId());
        budgetGroup.updateListOfMembers(Set.of(userFromContext.getId()));

        return budgetGroup;
    }
}