package com.patrykdankowski.financeflock.expense;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
class ExpenseFacade {

    ExpenseFacade(final ExpenseService expenseService,
                  final ExpenseManagementDomain expenseManagementDomain) {

        this.expenseService = expenseService;
        this.expenseManagementDomain = expenseManagementDomain;
    }


    private final ExpenseService expenseService;
    private final ExpenseManagementDomain expenseManagementDomain;


    @Transactional
    void addExpense(ExpenseDto expenseDto, String userIp) {

        final Expense expense = expenseManagementDomain.addExpense(expenseDto, userIp);

        expenseService.saveExpense(expense);

    }

    @Transactional
    void updateExpense(Long id, ExpenseDto expenseDto) {

        final Expense expenseToUpdate = expenseManagementDomain.updateExpense(id, expenseDto);
        expenseService.saveExpense(expenseToUpdate);
    }


}
