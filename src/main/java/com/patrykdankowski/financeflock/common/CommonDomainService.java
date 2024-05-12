package com.patrykdankowski.financeflock.common;

import com.patrykdankowski.financeflock.budgetgroup.BudgetGroup;
import com.patrykdankowski.financeflock.user.User;

public interface CommonDomainService {

     BudgetGroup validateIfGroupIsNotNullAndGetBudgetGroup(final User userFromContext);

     BudgetGroup validateAndGetUserGroup(final User userFromContext);

}
