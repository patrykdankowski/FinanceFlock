package com.patrykdankowski.financeflock.budgetgroup.port;

import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.model.record.BudgetGroupDescription;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;

public interface BudgetGroupFactoryPort {

    BudgetGroupDomainEntity createBudgetGroupFromRequest(UserDomainEntity userFromContext, BudgetGroupDescription budgetGroupDescription);
}
