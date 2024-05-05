package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.auth.AuthenticationService;
import com.patrykdankowski.financeflock.exception.ExpenseNotFoundException;
import com.patrykdankowski.financeflock.expense.dto.ExpenseDto;
import com.patrykdankowski.financeflock.expense.dto.ExpenseDtoWriteModel;
import com.patrykdankowski.financeflock.user.User;
import com.patrykdankowski.financeflock.user.dto.UserDto;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExpenseFacade {

    ExpenseFacade(
            final ExpenseManagementDomain expenseManagementDomain,
            final ExpenseCommandRepository expenseCommandRepository,
            final AuthenticationService authenticationService) {

        this.expenseManagementDomain = expenseManagementDomain;
        this.expenseCommandRepository = expenseCommandRepository;
        this.authenticationService = authenticationService;
    }


    private final ExpenseManagementDomain expenseManagementDomain;
    private final ExpenseCommandRepository expenseCommandRepository;
    private final AuthenticationService authenticationService;


    @Transactional
    void addExpense(ExpenseDtoWriteModel expenseDtoWriteModel, String userIp) {

        final Expense expense = expenseManagementDomain.addExpense(expenseDtoWriteModel, userIp);
        expenseCommandRepository.save(expense);

    }

    @Transactional
    void updateExpense(Long id, ExpenseDtoWriteModel expenseDtoWriteModel) {

        ExpenseDto expenseDto = retrieveExpenseById(id).toDto();
        User userFromContext = authenticationService.getUserFromContext();
        UserDto userFromContextDto = userFromContext.toDto();

         ExpenseDto expenseToUpdate = expenseManagementDomain.updateExpense(expenseDtoWriteModel, expenseDto, userFromContextDto);

        expenseCommandRepository.save(Expense.fromDto(expenseToUpdate));
    }

    private Expense retrieveExpenseById(final Long id) {
        return expenseCommandRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException(id));
    }


}
