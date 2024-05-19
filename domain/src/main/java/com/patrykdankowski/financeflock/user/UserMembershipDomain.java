package com.patrykdankowski.financeflock.user;

import com.patrykdankowski.financeflock.budgetgroup.BudgetGroup;

interface UserMembershipDomain {


    BudgetGroup leaveBudgetGroup(final User userFromContext);

    boolean toggleShareData(final User userFromContext);

}
