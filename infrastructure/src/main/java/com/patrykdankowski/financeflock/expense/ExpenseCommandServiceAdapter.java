package com.patrykdankowski.financeflock.expense;


import org.springframework.stereotype.Service;

@Service
public class ExpenseCommandServiceAdapter implements ExpenseCommandServicePort {

    private final ExpenseCommandRepository expenseCommandRepository;

    public ExpenseCommandServiceAdapter(final ExpenseCommandRepository expenseCommandRepository) {
        this.expenseCommandRepository = expenseCommandRepository;
    }

    @Override
    public Expense retrieveExpenseById(final Long id) {
        return expenseCommandRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException(id));
    }
}
