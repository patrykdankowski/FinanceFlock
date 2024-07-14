package com.patrykdankowski.financeflock.user.port;

import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;

public interface UserMembershipDomainPort {


    void leaveBudgetGroup(final UserDomainEntity userFromContext,
                          final BudgetGroupDomainEntity budgetGroup,
                          final Long id);

    boolean toggleShareData(final UserDomainEntity userFromContext);

}
