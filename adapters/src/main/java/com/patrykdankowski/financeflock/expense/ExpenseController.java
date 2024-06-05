package com.patrykdankowski.financeflock.expense;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
@Slf4j
class ExpenseController implements ExpenseControllerApi {

    private final ExpenseFacade expenseFacade;
    private final ExpenseGeolocationServicePort geolocationService;

    @Override
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String addExpense(@Validated(ExpenseDtoWriteModel.onCreate.class) @RequestBody ExpenseDtoWriteModel expenseDtoWriteModel,
                             HttpServletRequest request) {

        String userIp = geolocationService.getUserIpAddress(request);
        final long expenseId = expenseFacade.addExpense(expenseDtoWriteModel, userIp);

        return String.format("Expense created with id %d", expenseId);

    }

    @Override
    @PatchMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String updateExpense(@PathVariable Long id,
                                @RequestBody ExpenseDtoWriteModel expenseDtoWriteModel) {

        expenseFacade.updateExpense(id, expenseDtoWriteModel);
        return "Resource updated";
    }
}
