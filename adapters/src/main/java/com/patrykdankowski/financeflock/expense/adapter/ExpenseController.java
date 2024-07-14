package com.patrykdankowski.financeflock.expense.adapter;

import com.patrykdankowski.financeflock.expense.dto.ExpenseDtoWriteModel;
import com.patrykdankowski.financeflock.expense.port.ExpenseControllerApi;
import com.patrykdankowski.financeflock.expense.port.ExpenseFacade;
import com.patrykdankowski.financeflock.expense.port.ExpenseGeolocationServicePort;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public String addExpense(@Validated(ExpenseDtoWriteModel.onCreate.class) @Valid @RequestBody ExpenseDtoWriteModel expenseDtoWriteModel,
                             HttpServletRequest request) {

        String userIp = geolocationService.getUserIpAddress(request);
        final long expenseId = expenseFacade.create(expenseDtoWriteModel, userIp);

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

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpense(@PathVariable Long id) {
        expenseFacade.deleteExpense(id);
    }
}
