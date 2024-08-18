package com.patrykdankowski.financeflock.expense_category.adapter;

import com.patrykdankowski.financeflock.expense_category.model.entity.ExpenseCategoryDomainEntity;
import com.patrykdankowski.financeflock.expense_category.model.record.ExpenseCategoryName;
import com.patrykdankowski.financeflock.expense_category.port.ExpenseCategoryFactoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
class ExpenseCategoryFactoryAdapter implements ExpenseCategoryFactoryPort {
    private static final Logger log = LoggerFactory.getLogger(ExpenseCategoryFactoryAdapter.class);

    @Override
    public ExpenseCategoryDomainEntity createExpenseCategoryFromRequest(final ExpenseCategoryName categoryName, final Long budgetGroupId) {
        return ExpenseCategoryDomainEntity.buildExpenseCategory(
                null,
                categoryName.categoryName(),
                budgetGroupId,
                false
        );
    }
}
