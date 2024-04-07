package com.patrykdankowski.financeflock.service;

import com.patrykdankowski.financeflock.dto.ExpenseDto;
import com.patrykdankowski.financeflock.entity.Expense;
import com.patrykdankowski.financeflock.entity.User;
import com.patrykdankowski.financeflock.repository.ExpenseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final GeolocationService geolocationService;
    private final UserCacheService userCacheService;
    private final UserContextService userContextService;


    @Transactional
    public void addExpense(ExpenseDto expenseDto, String userIp) {
        Authentication authentication = userContextService.getAuthentication();
        String userEmail = authentication.getName();
        User user = userCacheService.getUserFromEmail(userEmail);
        if (expenseDto.getLocation() == null || expenseDto.getLocation().isEmpty()) {
            try {
                String city = geolocationService.getLocationFromUserIp(userIp);
                expenseDto.setLocation(city);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        LocalDateTime expenseDate = expenseDto.getExpenseDate() == null ? LocalDateTime.now() : expenseDto.getExpenseDate();

        Expense expense = Expense.builder()
                .expenseDate(expenseDate)
                .amount(expenseDto.getAmount())
                .description(expenseDto.getDescription())
                .location(expenseDto.getLocation())
                .user(user)
                .build();

        expenseRepository.save(expense);

    }

}
