package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.auth.AuthenticationService;
import com.patrykdankowski.financeflock.exception.ExpenseNotFoundException;
import com.patrykdankowski.financeflock.expense.dto.ExpenseDtoWriteModel;
import com.patrykdankowski.financeflock.user.User;
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
    long addExpense(ExpenseDtoWriteModel expenseDtoWriteModel, String userIp) {

        final User userFromContext = authenticationService.getUserFromContext();
        final Expense expense = expenseManagementDomain.addExpense(expenseDtoWriteModel, userIp, userFromContext);
        return expenseCommandRepository.save(expense).getId();


    }

    @Transactional
    void updateExpense(Long id, ExpenseDtoWriteModel expenseSourceDto) {

        final Expense expense = retrieveExpenseById(id);
        final User userFromContext = authenticationService.getUserFromContext();

        expenseManagementDomain.updateExpense(expenseSourceDto,
                expense,
                userFromContext);

        expenseCommandRepository.save(expense);
    }

    private Expense retrieveExpenseById(final Long id) {
        return expenseCommandRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException(id));
    }


}
