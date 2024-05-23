package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.UserDomainEntity;

public interface CommonDomainServicePort {

     BudgetGroupDomainEntity validateAndGetGroup(final UserDomainEntity userFromContext, final Long id);

     BudgetGroupDomainEntity validateAndGetUserGroup(final UserDomainEntity userFromContext);

}
