package com.patrykdankowski.financeflock.expense.port;


import com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity;
import com.patrykdankowski.financeflock.expense.model.vo.ExpenseValueObject;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;

public interface ExpenseManagementDomainPort {
    ExpenseDomainEntity createExpense(final ExpenseValueObject expenseDtoWriteModel,
                                      final UserDomainEntity loggedUser);

    void updateExpense(final ExpenseValueObject expenseDtoWriteModel,
                       final ExpenseDomainEntity expenseDomainEntity);


}
