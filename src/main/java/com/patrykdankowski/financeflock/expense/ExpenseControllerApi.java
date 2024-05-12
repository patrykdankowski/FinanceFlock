package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.expense.dto.ExpenseDtoWriteModel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;

public interface ExpenseControllerApi {

    @PostMapping("/add")
     ResponseEntity<URI> addExpense(@Validated(ExpenseDtoWriteModel.onCreate.class) @RequestBody ExpenseDtoWriteModel expenseDtoWriteModel,
                                          HttpServletRequest request);

    @PatchMapping("/update/{id}")
    ResponseEntity<String> updateExpense(@PathVariable Long id,
                                         @RequestBody ExpenseDtoWriteModel expenseDtoWriteModel);
}
