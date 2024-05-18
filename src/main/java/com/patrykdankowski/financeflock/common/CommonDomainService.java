package com.patrykdankowski.financeflock.common;

import com.patrykdankowski.financeflock.budgetgroup.BudgetGroup;
import com.patrykdankowski.financeflock.user.User;

public interface CommonDomainService {

     BudgetGroup validateAndGetGroup(final User userFromContext,final Long id);

     BudgetGroup validateAndGetUserGroup(final User userFromContext);

}
