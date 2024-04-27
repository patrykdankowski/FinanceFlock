package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.auth.AuthenticationService;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.exception.ExpenseNotFoundException;
import com.patrykdankowski.financeflock.exception.ResourceNotBelongToUserException;
import com.patrykdankowski.financeflock.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ExpenseManagementDomainImpl implements ExpenseManagementDomain {
    private final AuthenticationService authenticationService;
    private final ExpenseGeolocationService geolocationService;
    private final ExpenseService expenseService;

    public ExpenseManagementDomainImpl(final AuthenticationService authenticationService, final ExpenseGeolocationServiceImpl geolocationService, final ExpenseService expenseService) {
        this.authenticationService = authenticationService;
        this.geolocationService = geolocationService;
        this.expenseService = expenseService;
    }

    @Override
    public Expense addExpense(final ExpenseDto expenseDto, final String userIp) {
        final User userFromContext = authenticationService.getUserFromContext();

        validateAndPrepareExpense(expenseDto, userIp);

        final Expense expense = createExpense(expenseDto, userFromContext);
        userFromContext.addExpense(expense);

        return expense;
    }

    private Expense createExpense(final ExpenseDto expenseDto, final User userFromContext) {
        return Expense.builder()
                .expenseDate(expenseDto.getExpenseDate())
                .amount(expenseDto.getAmount())
                .description(expenseDto.getDescription())
                .location(expenseDto.getLocation())
                // owner jest ustawiany przy wywołaniu metody .addExpense()
                .build();
    }

    private void validateAndPrepareExpense(final ExpenseDto expenseDto, final String userIp) {
        if (expenseDto.getLocation() == null || expenseDto.getLocation().isEmpty()) {
            setLocationForExpenseFromUserIp(expenseDto, userIp);

        }
        if (expenseDto.getExpenseDate() == null) {
            expenseDto.setExpenseDate(LocalDateTime.now());
        }


    }

    private void setLocationForExpenseFromUserIp(final ExpenseDto expenseDto, final String userIp) {
        try {
            String city = geolocationService.getLocationFromUserIp(userIp);
            expenseDto.setLocation(city);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Expense updateExpense(final Long id, final ExpenseDto expenseDto) {
        final User userFromContext = authenticationService.getUserFromContext();
        final Expense toUpdate = expenseService.getExpenseById(id);


        validateUserAccessToExpense(id, userFromContext, toUpdate);
        final Expense expense = validateAndSetFieldsForExpense(expenseDto, toUpdate);
        return expense;
    }

    private Expense validateAndSetFieldsForExpense(final ExpenseDto expenseDto, final Expense toUpdate) {
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
        return toUpdate;
    }

    private void validateUserAccessToExpense(final Long id, final User userFromContext, final Expense toUpdate) {

        boolean isExpenseInSameUserGroup =userFromContext.getRole().equals(Role.GROUP_ADMIN) && userFromContext.getBudgetGroup().equals(toUpdate.getUser().getBudgetGroup());
        boolean isExpenseOfUser = userFromContext.getExpenseList().contains(toUpdate) && toUpdate.getUser().equals(userFromContext);


        if (isExpenseInSameUserGroup && !isExpenseOfUser) {
            throw new ResourceNotBelongToUserException(toUpdate.getUser().getName(), toUpdate.getId());
        } else if (!isExpenseOfUser) {
            throw new ExpenseNotFoundException(id);

        }
    }
}

