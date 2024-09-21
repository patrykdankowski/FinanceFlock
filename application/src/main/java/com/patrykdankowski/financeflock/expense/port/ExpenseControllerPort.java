package com.patrykdankowski.financeflock.expense.port;

import com.patrykdankowski.financeflock.expense.dto.ExpenseCreateDto;
import com.patrykdankowski.financeflock.expense.dto.ExpenseUpdateDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;


public interface ExpenseControllerPort {

    String addExpense(ExpenseCreateDto expenseCreateDto, HttpServletRequest request);

    @PatchMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    String updateExpense(@PathVariable Long id,
                         @RequestBody ExpenseUpdateDto expenseUpdateDto);

    void deleteExpense(Long id);
}
