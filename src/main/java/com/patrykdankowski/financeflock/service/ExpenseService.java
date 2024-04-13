package com.patrykdankowski.financeflock.service;

import com.patrykdankowski.financeflock.constants.AppConstants;
import com.patrykdankowski.financeflock.dto.ExpenseDto;
import com.patrykdankowski.financeflock.entity.Expense;
import com.patrykdankowski.financeflock.entity.Role;
import com.patrykdankowski.financeflock.entity.User;
import com.patrykdankowski.financeflock.exception.ResourceNotBelongToUserException;
import com.patrykdankowski.financeflock.exception.ResourceNotFoundException;
import com.patrykdankowski.financeflock.repository.ExpenseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.patrykdankowski.financeflock.constants.AppConstants.USER_NOT_FOUND;

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

    @Transactional
    public void updateExpense(Long id, ExpenseDto expenseDto) {
        Authentication authentication = userContextService.getAuthentication();
        String userEmail = authentication.getName();
        User user = userCacheService.getUserFromEmail(userEmail);
        Expense toUpdate = expenseRepository.findById(id)

                .orElseThrow(() -> new ResourceNotFoundException(id, AppConstants.EXPENSE_NOT_FOUND));


        if (user.getRole().equals(Role.GROUP_ADMIN) && (!user.getExpenseList().contains(toUpdate) || !toUpdate.getUser().equals(user))) {
            throw new ResourceNotBelongToUserException(userEmail, toUpdate.getId());
        }
        else if (!user.getExpenseList().contains(toUpdate) || !toUpdate.getUser().equals(user))
        {
            throw new ResourceNotFoundException(id, AppConstants.EXPENSE_NOT_FOUND);
        }

        if (expenseDto.getExpenseDate() != null) {
            toUpdate.setExpenseDate(expenseDto.getExpenseDate());
        }
        if (expenseDto.getDescription() != null && !expenseDto.getDescription().isBlank()) {
            toUpdate.setDescription(expenseDto.getDescription());
        }
        if (expenseDto.getAmount() != null) {
            toUpdate.setAmount(expenseDto.getAmount());
        }
        if (expenseDto.getLocation() != null && !expenseDto.getLocation().isBlank()) {
            toUpdate.setLocation(expenseDto.getLocation());
        }
        expenseRepository.save(toUpdate);

    }

}
