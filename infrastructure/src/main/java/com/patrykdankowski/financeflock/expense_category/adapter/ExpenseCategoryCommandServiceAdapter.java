package com.patrykdankowski.financeflock.expense_category.adapter;

import com.patrykdankowski.financeflock.expense_category.exception.ExpenseCategoryNotFoundException;
import com.patrykdankowski.financeflock.expense_category.model.entity.ExpenseCategoryDomainEntity;
import com.patrykdankowski.financeflock.expense_category.port.ExpenseCategoryCommandRepositoryPort;
import com.patrykdankowski.financeflock.expense_category.port.ExpenseCategoryCommandServicePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
class ExpenseCategoryCommandServiceAdapter implements ExpenseCategoryCommandServicePort {

//    private final ExpenseCategoryCommandRepositoryPort expenseCategoryCommandRepository;
//
//    ExpenseCategoryCommandServiceAdapter(final ExpenseCategoryCommandRepositoryPort expenseCategoryCommandRepository) {
//        this.expenseCategoryCommandRepository = expenseCategoryCommandRepository;
//    }
//
//    @Override
//    public ExpenseCategoryDomainEntity saveExpenseCategory(final ExpenseCategoryDomainEntity expenseCategory) {
//        return expenseCategoryCommandRepository.save(expenseCategory);
//    }
//
//    @Override
//    public ExpenseCategoryDomainEntity getExpenseCategoryById(final Long id) {
//        return expenseCategoryCommandRepository.findById(id)
//                .orElseThrow(() ->
//                {
//                    log.warn("Could not find expense category with id {}", id);
//                    return new ExpenseCategoryNotFoundException(id);
//                });
//    }

}
