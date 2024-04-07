package com.patrykdankowski.financeflock.controller;

import com.patrykdankowski.financeflock.dto.ExpenseDto;
import com.patrykdankowski.financeflock.service.CacheService;
import com.patrykdankowski.financeflock.service.ExpenseService;
import com.patrykdankowski.financeflock.service.GeolocationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
@Slf4j
public class ExpenseController {

    private final ExpenseService expenseService;
    private final GeolocationService geolocationService;
    private final CacheService cacheService;

    @PostMapping("/add")
    public ResponseEntity<String> addExpense(@Valid @RequestBody ExpenseDto expenseDto,
                                             HttpServletRequest request) {

        String userIp = geolocationService.getUserIpAddress(request);
        expenseService.addExpense(expenseDto, userIp);
        log.info(cacheService.cachedObjects("userEmailCache").toString());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Expense added successfully");
    }
}
