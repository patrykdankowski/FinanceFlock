package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.UserDomainEntity;

class BudgetGroupFactory {

    public BudgetGroupDomainEntity createBudgetGroupFromRequest(UserDomainEntity userFromContext, BudgetGroupRequest budgetGroupRequest) {

        return BudgetGroupDomainEntity.builder()
                .owner(userFromContext)
                .description(budgetGroupRequest.getDescription())
                .member(userFromContext)
                .build();

    }
}