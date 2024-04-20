package com.patrykdankowski.financeflock.expense;

import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public Optional<Expense> findExpenseById(final long id) {
        return expenseRepository.findById(id);
    }
}
