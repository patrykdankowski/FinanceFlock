package com.patrykdankowski.financeflock.expense_category.adapter;

import com.patrykdankowski.financeflock.expense_category.model.entity.ExpenseCategoryDomainEntity;
import com.patrykdankowski.financeflock.expense_category.port.ExpenseCategoryCommandRepositoryPort;
import com.patrykdankowski.financeflock.expense_category.port.ExpenseCategoryCommandServicePort;
import org.springframework.stereotype.Service;

@Service
class ExpenseCategoryCommandServiceAdapter implements ExpenseCategoryCommandServicePort {

    private final ExpenseCategoryCommandRepositoryPort expenseCategoryCommandRepository;

    ExpenseCategoryCommandServiceAdapter(final ExpenseCategoryCommandRepositoryPort expenseCategoryCommandRepository) {
        this.expenseCategoryCommandRepository = expenseCategoryCommandRepository;
    }

    @Override
    public ExpenseCategoryDomainEntity saveExpenseCategory(final ExpenseCategoryDomainEntity expenseCategory) {
        return expenseCategoryCommandRepository.save(expenseCategory);
    }

}
