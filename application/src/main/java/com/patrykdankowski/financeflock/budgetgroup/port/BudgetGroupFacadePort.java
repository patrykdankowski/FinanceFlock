package com.patrykdankowski.financeflock.budgetgroup.port;

import com.patrykdankowski.financeflock.budgetgroup.dto.BudgetGroupDto;
import com.patrykdankowski.financeflock.budgetgroup.dto.EmailDto;

public interface BudgetGroupFacadePort {

    Long createBudgetGroup(BudgetGroupDto budgetGroupDto);

    void closeBudgetGroup(Long id);

    void addUserToGroup(final EmailDto email, final Long id);

    void removeUserFromGroup(final EmailDto email, final Long id);
}
