package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.expense.dto.ExpenseDto;
import com.patrykdankowski.financeflock.expense.dto.ExpenseDtoWriteModel;
import com.patrykdankowski.financeflock.user.dto.UserDto;

interface ExpenseManagementDomain {
    Expense addExpense(ExpenseDtoWriteModel expenseDtoWriteModel, String userIp);

    ExpenseDto updateExpense(ExpenseDtoWriteModel id,
                             ExpenseDto expenseDtoWriteModel,
                             UserDto userFromContextDto);
}
