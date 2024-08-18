package com.patrykdankowski.financeflock.expense.adapter;


import com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity;
import com.patrykdankowski.financeflock.expense.exception.ExpenseNotFoundException;
import com.patrykdankowski.financeflock.expense.port.ExpenseCommandRepositoryPort;
import com.patrykdankowski.financeflock.expense.port.ExpenseCommandServicePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class ExpenseCommandServiceAdapter implements ExpenseCommandServicePort {

    private final ExpenseCommandRepositoryPort expenseCommandRepository;

    public ExpenseCommandServiceAdapter(final ExpenseCommandRepositoryPort expenseCommandRepository) {
        this.expenseCommandRepository = expenseCommandRepository;
    }

    @Override
    public ExpenseDomainEntity findExpenseById(final Long id) {
        return expenseCommandRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Could not find budget group with id {}", id);
                    return new ExpenseNotFoundException(id);
                });
    }

    @Override
    public ExpenseDomainEntity saveExpense(final ExpenseDomainEntity expenseDomainEntity) {
        return expenseCommandRepository.save(expenseDomainEntity);
    }

    @Override
    public void deleteExpense(final Long id) {
        expenseCommandRepository.deleteById(id);
    }
}
