package com.patrykdankowski.financeflock.expense.port;

import com.patrykdankowski.financeflock.expense.dto.ExpenseCreateDto;
import jakarta.servlet.http.HttpServletRequest;


public interface ExpenseControllerPort {

    String addExpense(ExpenseCreateDto expenseCreateDto, HttpServletRequest request);

    String updateExpense(Long id, ExpenseCreateDto expenseCreateDto);

    void deleteExpense(Long id);
}
