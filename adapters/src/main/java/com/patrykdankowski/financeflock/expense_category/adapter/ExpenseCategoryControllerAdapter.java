package com.patrykdankowski.financeflock.expense_category.adapter;

import com.patrykdankowski.financeflock.auth.port.AuthenticationServicePort;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupCommandServicePort;
import com.patrykdankowski.financeflock.expense_category.dto.ExpenseCategoryDto;
import com.patrykdankowski.financeflock.expense_category.port.ExpenseCategoryControllerPort;
import com.patrykdankowski.financeflock.expense_category.port.ExpenseCategoryFacadePort;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/expenseCategories")
@Slf4j
@RequiredArgsConstructor
class ExpenseCategoryControllerAdapter implements ExpenseCategoryControllerPort {

    private final AuthenticationServicePort authenticationService;
    private final ExpenseCategoryFacadePort expenseCategoryFacade;
    private final BudgetGroupCommandServicePort budgetGroupCommandService;


    @Override
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public String createCategory(@Valid @RequestBody ExpenseCategoryDto expenseCategoryDto) {

        final Long categoryId = expenseCategoryFacade.createCategory(expenseCategoryDto);
        return String.format("Category created with id %d", categoryId);
    }

    @GetMapping("/list/{id}")
    public Set<Long> listAllCategories(@PathVariable Long id) {
        final BudgetGroupDomainEntity budgetGroup = budgetGroupCommandService.findBudgetGroupById(id);
        return budgetGroup.getListOfCategoriesId();
    }
}
