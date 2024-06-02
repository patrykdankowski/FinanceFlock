package com.patrykdankowski.financeflock.user;

import com.patrykdankowski.financeflock.budgetgroup.BudgetGroupDomainEntity;

interface UserMembershipDomainPort {


    void leaveBudgetGroup(final UserDomainEntity userFromContext, final BudgetGroupDomainEntity budgetGroup, final Long id);

    boolean toggleShareData(final UserDomainEntity userFromContext);

}
