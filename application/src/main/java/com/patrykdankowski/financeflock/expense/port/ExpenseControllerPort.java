package com.patrykdankowski.financeflock.expense.port;

import com.patrykdankowski.financeflock.expense.dto.ExpenseDto;
import jakarta.servlet.http.HttpServletRequest;


public interface ExpenseControllerPort {

    String addExpense(ExpenseDto expenseDto, HttpServletRequest request);

    String updateExpense(Long id, ExpenseDto expenseDto);

    void deleteExpense(Long id);
}
