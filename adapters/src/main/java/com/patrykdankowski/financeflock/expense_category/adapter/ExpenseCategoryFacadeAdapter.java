package com.patrykdankowski.financeflock.expense_category.adapter;

import com.patrykdankowski.financeflock.auth.port.AuthenticationServicePort;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupCommandServicePort;
import com.patrykdankowski.financeflock.expense_category.dto.ExpenseCategoryDto;
import com.patrykdankowski.financeflock.expense_category.model.entity.ExpenseCategoryDomainEntity;
import com.patrykdankowski.financeflock.expense_category.model.record.ExpenseCategoryName;
import com.patrykdankowski.financeflock.expense_category.port.ExpenseCategoryCommandServicePort;
import com.patrykdankowski.financeflock.expense_category.port.ExpenseCategoryFacadePort;
import com.patrykdankowski.financeflock.expense_category.port.ExpenseCategoryManagementDomainPort;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class ExpenseCategoryFacadeAdapter implements ExpenseCategoryFacadePort {

//    private final AuthenticationServicePort authenticationService;
//    private final BudgetGroupCommandServicePort budgetGroupCommandService;
//    private final ExpenseCategoryManagementDomainPort expenseCategoryManagementDomain;
//    private final ExpenseCategoryCommandServicePort expenseCategoryCommandService;
//
//    public ExpenseCategoryFacadeAdapter(final AuthenticationServicePort authenticationService,
//                                        final BudgetGroupCommandServicePort budgetGroupCommandService, final ExpenseCategoryManagementDomainPort expenseCategoryManagementDomain, final ExpenseCategoryCommandServicePort expenseCategoryCommandService) {
//        this.authenticationService = authenticationService;
//        this.budgetGroupCommandService = budgetGroupCommandService;
//        this.expenseCategoryManagementDomain = expenseCategoryManagementDomain;
//        this.expenseCategoryCommandService = expenseCategoryCommandService;
//    }
//
//    @Override
//    @Transactional
//    public Long createCategory(final ExpenseCategoryDto expenseCategoryDto) {
//        final UserDomainEntity loggedUser = authenticationService.getUserFromContext();
//        final BudgetGroupDomainEntity budgetGroup = budgetGroupCommandService.findBudgetGroupById(loggedUser.getBudgetGroupId());
//        final ExpenseCategoryName expenseCategoryName = new ExpenseCategoryName(expenseCategoryDto.getCategoryName());
//         /*porównywanie czy nazwa z dto przypadkiem nie istnieje juz albo jej synonim
//         za pomoca gpt - przekazujemy nazwe z dto i kolekcje z encji
//         jezeli nie*/
//        final ExpenseCategoryDomainEntity expenseCategory = expenseCategoryManagementDomain.createExpenseCategory(expenseCategoryName, budgetGroup.getId());
//
//        final ExpenseCategoryDomainEntity expenseCategorySaved = expenseCategoryCommandService.saveExpenseCategory(expenseCategory);
//        budgetGroup.addCategory(expenseCategorySaved.getId());
//        budgetGroupCommandService.saveBudgetGroup(budgetGroup);
//        return expenseCategorySaved.getId();
//
//    }
}
