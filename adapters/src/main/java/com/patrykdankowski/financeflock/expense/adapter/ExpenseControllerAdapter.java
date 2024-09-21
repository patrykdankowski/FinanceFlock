package com.patrykdankowski.financeflock.expense.adapter;

import com.patrykdankowski.financeflock.expense.dto.ExpenseCreateDto;
import com.patrykdankowski.financeflock.expense.dto.ExpenseUpdateDto;
import com.patrykdankowski.financeflock.expense.port.ExpenseControllerPort;
import com.patrykdankowski.financeflock.expense.port.ExpenseFacadePort;
import com.patrykdankowski.financeflock.external_api.ExpenseGeolocationServicePort;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
@Slf4j
class ExpenseControllerAdapter implements ExpenseControllerPort {

    private final ExpenseFacadePort expenseFacade;
    private final ExpenseGeolocationServicePort geolocationService;

    @Override
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String addExpense(@Valid @RequestBody ExpenseCreateDto expenseCreateDto,
                             HttpServletRequest request) {

        String userIp = geolocationService.getUserIpAddress(request);
        final long expenseId = expenseFacade.createExpense(expenseCreateDto, userIp);

        return String.format("Expense created with id %d", expenseId);

    }

    @Override
    @PatchMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String updateExpense(@PathVariable Long id,
                                @RequestBody ExpenseUpdateDto expenseUpdateDto) {

        expenseFacade.updateExpense(id, expenseUpdateDto);
        return "Resource updated";
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpense(@PathVariable Long id) {
        expenseFacade.deleteExpense(id);
    }
}
