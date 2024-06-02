package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.user.UserDomainEntity;

interface ExpenseManagementDomainPort {
    ExpenseDomainEntity createExpense(final ExpenseDtoWriteModel expenseDtoWriteModel,
                                      final UserDomainEntity userFromContext);


//    void updateExpense(ExpenseDtoWriteModel expenseSourceDto,
//                       ExpenseDomainEntity expenseDomainEntity,
//                       UserDomainEntity userFromContext);

    void validateUserAccessToExpense(final UserDomainEntity userFromContext,
                                     final ExpenseDomainEntity expenseDomainEntity, final Long expenseUserGroupId);

    void validateAndSetFieldsForExpense(final ExpenseDtoWriteModel expenseDtoWriteModel,
                                        final ExpenseDomainEntity expenseDomainEntity);
}
