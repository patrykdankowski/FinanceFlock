package com.patrykdankowski.financeflock.expense_category.adapter;

import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.expense_category.model.entity.ExpenseCategoryDomainEntity;
import com.patrykdankowski.financeflock.expense_category.model.record.ExpenseCategoryName;
import com.patrykdankowski.financeflock.expense_category.port.ExpenseCategoryFactoryPort;
import com.patrykdankowski.financeflock.expense_category.port.ExpenseCategoryManagementDomainPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class ExpenseCategoryManagementDomainAdapter implements ExpenseCategoryManagementDomainPort {

    private final ExpenseCategoryFactoryPort expenseCategoryFactory;

    ExpenseCategoryManagementDomainAdapter(final ExpenseCategoryFactoryPort expenseCategoryFactory) {
        this.expenseCategoryFactory = expenseCategoryFactory;
    }


    @Override
    public ExpenseCategoryDomainEntity createExpenseCategory(final ExpenseCategoryName categoryName, final Long budgetGroupId) {
        return expenseCategoryFactory.createExpenseCategoryFromRequest(categoryName, budgetGroupId);
    }
}
