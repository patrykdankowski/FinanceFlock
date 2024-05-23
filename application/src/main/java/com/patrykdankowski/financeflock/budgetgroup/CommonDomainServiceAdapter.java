package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.Logger;
import com.patrykdankowski.financeflock.user.UserDomainEntity;

public class CommonDomainServiceAdapter implements CommonDomainServicePort {

    private final org.slf4j.Logger logger = Logger.getLogger(this.getClass());


    @Override
    public BudgetGroupDomainEntity validateAndGetGroup(final UserDomainEntity userFromContext, final Long id) {
        var groupToValidate = userFromContext.getBudgetGroup();
        if (userFromContext.getBudgetGroup()== null) {
            logger.warn("User {} is not a member of any group", userFromContext.getName());
            throw new IllegalStateException("User does not belong to any budget group");
        }
        if (!groupToValidate.getId().equals(id)) {
            logger.warn("Given id group {} is not the same as your group", id);
            throw new IllegalStateException("U are not a member of given id group");
        }
        return groupToValidate;
    }

    @Override
    public BudgetGroupDomainEntity validateAndGetUserGroup(final UserDomainEntity userFromContext) {
        BudgetGroupDomainEntity budgetGroupDomainEntity = userFromContext.getBudgetGroup();
        if (budgetGroupDomainEntity == null) {
            logger.warn("Budget group is null");
            throw new IllegalStateException("Budget group does not exist");
        }
        return budgetGroupDomainEntity;
    }
}
