package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupFactoryPort;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.model.record.BudgetGroupDescription;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
class BudgetGroupFactoryAdapter implements BudgetGroupFactoryPort {

    public BudgetGroupDomainEntity createBudgetGroupFromRequest(UserDomainEntity userFromContext, BudgetGroupDescription budgetGroupDescription) {

        var budgetGroup = new BudgetGroupDomainEntity(
                null, budgetGroupDescription.getValue(), userFromContext.getId());
        budgetGroup.updateListOfMembers(Set.of(userFromContext.getId()));

        return budgetGroup;
    }
}