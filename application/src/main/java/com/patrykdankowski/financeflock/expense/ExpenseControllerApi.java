package com.patrykdankowski.financeflock.expense;

import jakarta.servlet.http.HttpServletRequest;


interface ExpenseControllerApi {

    String addExpense(ExpenseDtoWriteModel expenseDtoWriteModel,
                      HttpServletRequest request);

    String updateExpense(Long id,
                         ExpenseDtoWriteModel expenseDtoWriteModel);
}
