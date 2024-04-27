package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.exception.ExpenseNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseServiceImpl(final ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public Expense saveExpense(final Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    public Expense getExpenseById(final long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException(id));
    }
}
