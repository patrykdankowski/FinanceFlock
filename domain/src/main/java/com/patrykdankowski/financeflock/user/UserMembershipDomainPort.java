package com.patrykdankowski.financeflock.user;

import com.patrykdankowski.financeflock.budgetgroup.BudgetGroupDomainEntity;

interface UserMembershipDomainPort {


    BudgetGroupDomainEntity leaveBudgetGroup(final UserDomainEntity userFromContext);

    boolean toggleShareData(final UserDomainEntity userFromContext);

}
