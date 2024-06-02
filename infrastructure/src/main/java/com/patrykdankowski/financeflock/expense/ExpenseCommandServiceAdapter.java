package com.patrykdankowski.financeflock.expense;


import org.springframework.stereotype.Service;

@Service
public class ExpenseCommandServiceAdapter implements ExpenseCommandServicePort {

    private final ExpenseCommandRepositoryPort expenseCommandRepository;

    public ExpenseCommandServiceAdapter(final ExpenseCommandRepositoryPort expenseCommandRepository) {
        this.expenseCommandRepository = expenseCommandRepository;
    }

    @Override
    public ExpenseDomainEntity findExpenseById(final Long id) {
        return expenseCommandRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException(id));
    }

    @Override
    public ExpenseDomainEntity saveExpense(final ExpenseDomainEntity expenseDomainEntity) {
        return expenseCommandRepository.save(expenseDomainEntity);
    }
}
