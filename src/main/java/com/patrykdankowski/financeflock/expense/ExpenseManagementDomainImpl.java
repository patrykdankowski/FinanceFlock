package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.auth.AuthenticationService;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.exception.ExpenseNotFoundException;
import com.patrykdankowski.financeflock.exception.ResourceNotBelongToUserException;
import com.patrykdankowski.financeflock.expense.dto.ExpenseDto;
import com.patrykdankowski.financeflock.expense.dto.ExpenseDtoWriteModel;
import com.patrykdankowski.financeflock.user.User;
import com.patrykdankowski.financeflock.user.dto.UserDto;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
class ExpenseManagementDomainImpl implements ExpenseManagementDomain {
    private final AuthenticationService authenticationService;
    private final ExpenseGeolocationService geolocationService;
    private final ExpenseQueryRepository expenseQueryRepository;

    public ExpenseManagementDomainImpl(final AuthenticationService authenticationService,
                                       final ExpenseGeolocationServiceImpl geolocationService,
                                       final ExpenseQueryRepository expenseQueryRepository) {
        this.authenticationService = authenticationService;
        this.geolocationService = geolocationService;
        this.expenseQueryRepository = expenseQueryRepository;
    }

    @Override
    public Expense addExpense(final ExpenseDtoWriteModel expenseDtoWriteModel, final String userIp) {
        var userFromContext = authenticationService.getUserFromContext();

        validateAndPrepareExpense(expenseDtoWriteModel, userIp);

        final Expense expense = createExpense(expenseDtoWriteModel, userFromContext);
        userFromContext.addExpense(expense);

        return expense;
    }

    private Expense createExpense(final ExpenseDtoWriteModel expenseDtoWriteModel, final User userFromContext) {
        return Expense.builder()
                .expenseDate(expenseDtoWriteModel.getExpenseDate())
                .amount(expenseDtoWriteModel.getAmount())
                .description(expenseDtoWriteModel.getDescription())
                .location(expenseDtoWriteModel.getLocation())
                // owner jest ustawiany przy wywołaniu metody .addExpense()
                .build();
    }

    private void validateAndPrepareExpense(final ExpenseDtoWriteModel expenseDtoWriteModel,
                                           final String userIp) {
        if (expenseDtoWriteModel.getLocation() == null || expenseDtoWriteModel.getLocation().isEmpty()) {
            setLocationForExpenseFromUserIp(expenseDtoWriteModel, userIp);

        }
        if (expenseDtoWriteModel.getExpenseDate() == null) {
            expenseDtoWriteModel.setExpenseDate(LocalDateTime.now());
        }


    }

    private void setLocationForExpenseFromUserIp(final ExpenseDtoWriteModel expenseDtoWriteModel,
                                                 final String userIp) {
        try {
            String city = geolocationService.getLocationFromUserIp(userIp);
            expenseDtoWriteModel.setLocation(city);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ExpenseDto updateExpense(final ExpenseDtoWriteModel expenseDtoWriteModel,
                                    final ExpenseDto expenseDto,
                                    final UserDto userFromContextDto) {


        validateUserAccessToExpense(userFromContextDto, expenseDto);

        ExpenseDto result = validateAndSetFieldsForExpense(expenseDtoWriteModel, expenseDto);
        result.setOwner(userFromContextDto);
        return result;
    }

    private void validateUserAccessToExpense(final UserDto userDtoFromContext, final ExpenseDto expenseDtoToUpdate) {

        //for group admin only
        boolean isExpenseInSameUserGroup = userDtoFromContext.getRole().equals(Role.GROUP_ADMIN) &&
                userDtoFromContext.getBudgetGroupId().equals(expenseDtoToUpdate.getOwnerGroupId());

        boolean isExpenseOfUser = userDtoFromContext.getExpenseListId().contains(expenseDtoToUpdate.getId()) &&
                expenseDtoToUpdate.getOwnerId().equals(userDtoFromContext.getId());


        if (isExpenseInSameUserGroup && !isExpenseOfUser) {
            throw new ResourceNotBelongToUserException(expenseDtoToUpdate.getOwnerId(), expenseDtoToUpdate.getId());
        } else if (!isExpenseOfUser) {
            throw new ExpenseNotFoundException(expenseDtoToUpdate.getId());

        }
    }

    private ExpenseDto validateAndSetFieldsForExpense(final ExpenseDtoWriteModel expenseDtoWriteModel,
                                                      final ExpenseDto expenseDto) {
        if (expenseDtoWriteModel.getExpenseDate() != null) {
            expenseDto.setExpenseDate(expenseDtoWriteModel.getExpenseDate());
        }
        if (expenseDtoWriteModel.getDescription() != null && !expenseDtoWriteModel.getDescription().isBlank()) {
            expenseDto.setDescription(expenseDtoWriteModel.getDescription());
        }
        if (expenseDtoWriteModel.getAmount() != null) {
            expenseDto.setAmount(expenseDtoWriteModel.getAmount());
        }
        if (expenseDtoWriteModel.getLocation() != null && !expenseDtoWriteModel.getLocation().isBlank()) {
            expenseDto.setLocation(expenseDtoWriteModel.getLocation());
        }
        return expenseDto;
    }
}

