package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.User;
import org.springframework.stereotype.Component;

@Component
public class BudgetGroupFactory {

    public BudgetGroup createBudgetGroupFromRequest(User userFromContext, BudgetGroupRequest budgetGroupRequest) {

        return BudgetGroup.builder()
                .owner(userFromContext)
                .description(budgetGroupRequest.getDescription())
                .listOfMembers(userFromContext)
                .build();

    }
}