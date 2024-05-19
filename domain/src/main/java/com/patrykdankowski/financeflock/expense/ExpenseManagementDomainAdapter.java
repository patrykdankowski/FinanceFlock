package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
class ExpenseManagementDomainAdapter implements ExpenseManagementDomainPort {


    @Override
    public Expense addExpense(final ExpenseDtoWriteModel expenseDtoWriteModel, final User userFromContext) {

        validateExpenseDate(expenseDtoWriteModel);

        final Expense expense = createExpense(expenseDtoWriteModel);
        userFromContext.addExpense(expense);

        return expense;
    }

    private Expense createExpense(final ExpenseDtoWriteModel expenseDtoWriteModel) {



        return Expense.builder()
                .expenseDate(expenseDtoWriteModel.getExpenseDate())
                .amount(expenseDtoWriteModel.getAmount())
                .description(expenseDtoWriteModel.getDescription())
                .location(expenseDtoWriteModel.getLocation())
                // owner jest ustawiany przy wywołaniu metody .addExpense()
                .build();
    }


    private void validateExpenseDate(final ExpenseDtoWriteModel expenseDtoWriteModel) {
        if (expenseDtoWriteModel.getExpenseDate() == null) {
            expenseDtoWriteModel.setExpenseDate(LocalDateTime.now());
        }
    }


    @Override
    public void updateExpense(final ExpenseDtoWriteModel expenseSourceDto,
                              final Expense expense,
                              final User userFromContext) {


        validateUserAccessToExpense(userFromContext, expense);

        validateAndSetFieldsForExpense(expenseSourceDto, expense);
    }

    private void validateUserAccessToExpense(final User userFromContext,
                                             final Expense expense) {

        //for group admin only
        boolean isExpenseInSameUserGroup = userFromContext.getRole().equals(Role.GROUP_ADMIN) &&
                userFromContext.getBudgetGroup().getId().equals(expense.getUser().getBudgetGroup().getId());

        boolean isExpenseOfUser = userFromContext.getExpenseList().contains(expense) &&
                expense.getUser().getId().equals(userFromContext.getId());


        if (isExpenseInSameUserGroup && !isExpenseOfUser) {
            throw new ExpenseNotBelongToUserException(userFromContext.getId(), expense.getId());
        } else if (!isExpenseOfUser) {
            throw new ExpenseNotFoundException(expense.getId());

        }
    }

    private void validateAndSetFieldsForExpense(final ExpenseDtoWriteModel expenseDtoWriteModel,
                                                final Expense expense) {
        if (expenseDtoWriteModel.getExpenseDate() != null) {
            expense.setExpenseDate(expenseDtoWriteModel.getExpenseDate());
        }
        if (expenseDtoWriteModel.getDescription() != null && !expenseDtoWriteModel.getDescription().isBlank()) {
            expense.setDescription(expenseDtoWriteModel.getDescription());
        }
        if (expenseDtoWriteModel.getAmount() != null) {
            expense.setAmount(expenseDtoWriteModel.getAmount());
        }
        if (expenseDtoWriteModel.getLocation() != null && !expenseDtoWriteModel.getLocation().isBlank()) {
            expense.setLocation(expenseDtoWriteModel.getLocation());
        }
    }
}

