package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.UserDomainEntity;

import java.util.HashSet;
import java.util.Set;

class BudgetGroupFactory {

    public BudgetGroupDomainEntity createBudgetGroupFromRequest(UserDomainEntity userFromContext, BudgetGroupRequest budgetGroupRequest) {

//        return BudgetGroupDomainEntity.builder()
//                .owner(userFromContext.getId())
//                .description(budgetGroupRequest.getDescription())
//                .member(userFromContext.getId())
//                .build();
        return new BudgetGroupDomainEntity(
                null, budgetGroupRequest.getDescription(), userFromContext.getId(), Set.of(userFromContext.getId()));

    }
}