package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.user.UserDomainEntity;

interface ExpenseManagementDomainPort {
    ExpenseDomainEntity createExpense(final ExpenseDtoWriteModel expenseDtoWriteModel,
                                      final UserDomainEntity userFromContext);


//    void updateExpense(ExpenseDtoWriteModel expenseSourceDto,
//                       ExpenseDomainEntity expenseDomainEntity,
//                       UserDomainEntity userFromContext);

    void validateUserAccessToExpense(final UserDomainEntity loggedUser,
                                     final UserDomainEntity userFromGivenIdExpense, final ExpenseDomainEntity expenseDomainEntity);

    void validateAndSetFieldsForExpense(final ExpenseDtoWriteModel expenseDtoWriteModel,
                                        final ExpenseDomainEntity expenseDomainEntity);
}
