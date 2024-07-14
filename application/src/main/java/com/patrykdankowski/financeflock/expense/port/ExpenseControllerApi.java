package com.patrykdankowski.financeflock.expense.port;

import com.patrykdankowski.financeflock.expense.dto.ExpenseDtoWriteModel;
import jakarta.servlet.http.HttpServletRequest;


public interface ExpenseControllerApi {

    String addExpense(ExpenseDtoWriteModel expenseDtoWriteModel,
                      HttpServletRequest request);

    String updateExpense(Long id,
                         ExpenseDtoWriteModel expenseDtoWriteModel);

    void deleteExpense(Long id);
}
