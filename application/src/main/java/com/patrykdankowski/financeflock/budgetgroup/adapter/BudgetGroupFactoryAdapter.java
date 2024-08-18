package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupFactoryPort;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.model.record.BudgetGroupDescription;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
class BudgetGroupFactoryAdapter implements BudgetGroupFactoryPort {

    public BudgetGroupDomainEntity createBudgetGroupFromRequest(Long userId, BudgetGroupDescription budgetGroupDescription) {

        var budgetGroup = BudgetGroupDomainEntity.buildBudgetGroup(
                null, budgetGroupDescription.getValue(), userId);
        budgetGroup.updateListOfMembers(Set.of(userId));

        return budgetGroup;
    }
}