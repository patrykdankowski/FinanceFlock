package com.patrykdankowski.financeflock.budgetgroup.port;

import com.patrykdankowski.financeflock.budgetgroup.dto.BudgetGroupRequest;
import com.patrykdankowski.financeflock.budgetgroup.dto.EmailRequest;

public interface BudgetGroupFacade {

    Long createBudgetGroup(BudgetGroupRequest budgetGroupRequest);

    void closeBudgetGroup(Long id);

    void addUserToGroup(final EmailRequest email, final Long id);

    void removeUserFromGroup(final EmailRequest email, final Long id);
}
