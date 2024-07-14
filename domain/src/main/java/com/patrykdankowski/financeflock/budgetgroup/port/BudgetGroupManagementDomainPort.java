package com.patrykdankowski.financeflock.budgetgroup.port;

import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.model.record.BudgetGroupDescription;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;

import java.util.List;

public interface BudgetGroupManagementDomainPort {


    BudgetGroupDomainEntity createBudgetGroup(BudgetGroupDescription budgetGroupRequest,
                                              final UserDomainEntity userFromContext);

    List<UserDomainEntity> closeBudgetGroup(final UserDomainEntity userFromContext,
                                            final Long id,
                                            final List<UserDomainEntity> listOfUsers,
                                            BudgetGroupDomainEntity budgetGroupDomainEntity);


}
