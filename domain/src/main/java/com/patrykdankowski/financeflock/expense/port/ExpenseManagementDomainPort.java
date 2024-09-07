package com.patrykdankowski.financeflock.expense.port;


import com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity;
import com.patrykdankowski.financeflock.expense.model.vo.ExpenseCreateVO;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;

public interface ExpenseManagementDomainPort {
    ExpenseDomainEntity createExpense(final ExpenseCreateVO expenseDtoWriteModel,
                                      final UserDomainEntity loggedUser);

    void updateExpense(final ExpenseCreateVO expenseDtoWriteModel,
                       final ExpenseDomainEntity expenseDomainEntity);


}
