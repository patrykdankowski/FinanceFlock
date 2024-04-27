package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.cache.CacheService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
@Slf4j
class ExpenseController {

    private final ExpenseFacade expenseFacade;
    private final ExpenseGeolocationServiceImpl geolocationService;
    private final CacheService cacheService;

    @PostMapping("/add")
    ResponseEntity<String> addExpense(@Validated(ExpenseDto.onCreate.class) @RequestBody ExpenseDto expenseDto,
                                             HttpServletRequest request) {

        String userIp = geolocationService.getUserIpAddress(request);
        expenseFacade.addExpense(expenseDto, userIp);
        log.info(cacheService.cachedObjects("userEmailCache").toString());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Expense added successfully");
    }
    @PatchMapping("/update/{id}")
    ResponseEntity<String> updateExpense(@PathVariable Long id,
                                                @RequestBody ExpenseDto expenseDto){

        expenseFacade.updateExpense(id,expenseDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Resource updated");
    }
}
