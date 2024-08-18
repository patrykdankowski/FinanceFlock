package com.patrykdankowski.financeflock.budgetgroup.port;

import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.model.record.BudgetGroupDescription;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;

import java.util.List;

public interface BudgetGroupManagementDomainPort {


    BudgetGroupDomainEntity createBudgetGroup(BudgetGroupDescription budgetGroupDescription,
                                              final Long userId);


    List<UserDomainEntity> closeBudgetGroup(List<UserDomainEntity> listOfUsers);
}
