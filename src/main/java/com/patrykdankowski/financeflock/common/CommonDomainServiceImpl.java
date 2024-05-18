package com.patrykdankowski.financeflock.common;

import com.patrykdankowski.financeflock.budgetgroup.BudgetGroup;
import com.patrykdankowski.financeflock.user.User;
import org.springframework.stereotype.Service;

@Service
public class CommonDomainServiceImpl implements CommonDomainService {

    private final org.slf4j.Logger logger = Logger.getLogger(this.getClass());


    @Override
    public BudgetGroup validateAndGetGroup(final User userFromContext, final Long id) {
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
    public BudgetGroup validateAndGetUserGroup(final User userFromContext) {
        BudgetGroup budgetGroup = userFromContext.getBudgetGroup();
        if (budgetGroup == null) {
            logger.warn("Budget group is null");
            throw new IllegalStateException("Budget group does not exist");
        }
        return budgetGroup;
    }
}
