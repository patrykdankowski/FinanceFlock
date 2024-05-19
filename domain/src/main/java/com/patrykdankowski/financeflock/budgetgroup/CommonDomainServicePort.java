package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.User;

public interface CommonDomainServicePort {

     BudgetGroup validateAndGetGroup(final User userFromContext, final Long id);

     BudgetGroup validateAndGetUserGroup(final User userFromContext);

}
