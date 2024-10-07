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


    String updateExpense(Long id,
                         ExpenseUpdateDto expenseUpdateDto);

    void deleteExpense(Long id);
}
