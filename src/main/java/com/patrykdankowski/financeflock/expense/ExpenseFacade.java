package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.constants.Role;
import com.patrykdankowski.financeflock.user.User;
import com.patrykdankowski.financeflock.exception.ResourceNotBelongToUserException;
import com.patrykdankowski.financeflock.exception.UserNotFoundException;
import com.patrykdankowski.financeflock.cache.UserCacheService;
import com.patrykdankowski.financeflock.auth.UserContextService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
class ExpenseFacade {

    ExpenseFacade(final ExpenseGeolocationService geolocationService, final UserCacheService userCacheService, final UserContextService userContextService, final ExpenseService expenseService) {
        this.geolocationService = geolocationService;
        this.userCacheService = userCacheService;
        this.userContextService = userContextService;
        this.expenseService = expenseService;
    }

    private final ExpenseGeolocationService geolocationService;
    private final UserCacheService userCacheService;
    private final UserContextService userContextService;
    private final ExpenseService expenseService;


    @Transactional
    void addExpense(ExpenseDto expenseDto, String userIp) {
        Authentication authentication = userContextService.getAuthenticationFromContext();
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
                .build();
        user.addExpense(expense);

        expenseService.saveExpense(expense);

    }

    @Transactional
    void updateExpense(Long id, ExpenseDto expenseDto) {
        Authentication authentication = userContextService.getAuthenticationFromContext();
        String userEmail = authentication.getName();
        User user = userCacheService.getUserFromEmail(userEmail);
        Expense toUpdate = expenseService.findExpenseById(id)

                .orElseThrow(() -> new UserNotFoundException(id));


        if (user.getRole().equals(Role.GROUP_ADMIN) && (!user.getExpenseList().contains(toUpdate) || !toUpdate.getUser().equals(user))) {
            throw new ResourceNotBelongToUserException(userEmail, toUpdate.getId());
        } else if (!user.getExpenseList().contains(toUpdate) || !toUpdate.getUser().equals(user)) {
            throw new UserNotFoundException(id);
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
        expenseService.saveExpense(toUpdate);

    }

}
