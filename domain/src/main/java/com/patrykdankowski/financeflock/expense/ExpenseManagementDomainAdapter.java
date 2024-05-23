package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.UserDomainEntity;

import java.time.LocalDateTime;


class ExpenseManagementDomainAdapter implements ExpenseManagementDomainPort {


    @Override
    public ExpenseDomainEntity addExpense(final ExpenseDtoWriteModel expenseDtoWriteModel, final UserDomainEntity userFromContext) {

        validateExpenseDate(expenseDtoWriteModel);

        final ExpenseDomainEntity expenseDomainEntity = createExpense(expenseDtoWriteModel);
        userFromContext.addExpense(expenseDomainEntity);

        return expenseDomainEntity;
    }

    private ExpenseDomainEntity createExpense(final ExpenseDtoWriteModel expenseDtoWriteModel) {



        return ExpenseDomainEntity.builder()
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
                              final ExpenseDomainEntity expenseDomainEntity,
                              final UserDomainEntity userFromContext) {


        validateUserAccessToExpense(userFromContext, expenseDomainEntity);

        validateAndSetFieldsForExpense(expenseSourceDto, expenseDomainEntity);
    }

    private void validateUserAccessToExpense(final UserDomainEntity userFromContext,
                                             final ExpenseDomainEntity expenseDomainEntity) {

        //for group admin only
        boolean isExpenseInSameUserGroup = userFromContext.getRole().equals(Role.GROUP_ADMIN) &&
                userFromContext.getBudgetGroup().getId().equals(expenseDomainEntity.getUser().getBudgetGroup().getId());

        boolean isExpenseOfUser = userFromContext.getExpenseList().contains(expenseDomainEntity) &&
                expenseDomainEntity.getUser().getId().equals(userFromContext.getId());


        if (isExpenseInSameUserGroup && !isExpenseOfUser) {
            throw new ExpenseNotBelongToUserException(userFromContext.getId(), expenseDomainEntity.getId());
        } else if (!isExpenseOfUser) {
            throw new ExpenseNotFoundException(expenseDomainEntity.getId());

        }
    }

    private void validateAndSetFieldsForExpense(final ExpenseDtoWriteModel expenseDtoWriteModel,
                                                final ExpenseDomainEntity expenseDomainEntity) {
        if (expenseDtoWriteModel.getExpenseDate() != null) {
            expenseDomainEntity.setExpenseDate(expenseDtoWriteModel.getExpenseDate());
        }
        if (expenseDtoWriteModel.getDescription() != null && !expenseDtoWriteModel.getDescription().isBlank()) {
            expenseDomainEntity.setDescription(expenseDtoWriteModel.getDescription());
        }
        if (expenseDtoWriteModel.getAmount() != null) {
            expenseDomainEntity.setAmount(expenseDtoWriteModel.getAmount());
        }
        if (expenseDtoWriteModel.getLocation() != null && !expenseDtoWriteModel.getLocation().isBlank()) {
            expenseDomainEntity.setLocation(expenseDtoWriteModel.getLocation());
        }
    }
}

