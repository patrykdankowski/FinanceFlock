package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.UserDomainEntity;

import java.time.LocalDateTime;


class ExpenseManagementDomainAdapter implements ExpenseManagementDomainPort {


    @Override
    public ExpenseDomainEntity createExpense(final ExpenseDtoWriteModel expenseDtoWriteModel,
                                             final UserDomainEntity userFromContext) {

        validateExpenseDate(expenseDtoWriteModel);

        return buildExpenseObject(expenseDtoWriteModel, userFromContext);

    }

    private ExpenseDomainEntity buildExpenseObject(final ExpenseDtoWriteModel expenseDtoWriteModel,
                                                   final UserDomainEntity userFromContext) {


        return ExpenseDomainEntity.builder()
                .expenseDate(expenseDtoWriteModel.getExpenseDate())
                .amount(expenseDtoWriteModel.getAmount())
                .description(expenseDtoWriteModel.getDescription())
                .location(expenseDtoWriteModel.getLocation())
                .userId(userFromContext.getId())
                .build();
    }


    private void validateExpenseDate(final ExpenseDtoWriteModel expenseDtoWriteModel) {
        if (expenseDtoWriteModel.getExpenseDate() == null) {
            expenseDtoWriteModel.setExpenseDate(LocalDateTime.now());
        }
    }

    @Override
    public void validateUserAccessToExpense(final UserDomainEntity userFromContext,
                                            final ExpenseDomainEntity expenseDomainEntity,
                                            final Long userGroupIdFromGivenExpenseId) {

        //for group admin only - different exception
        boolean isExpenseInSameUserGroup = userFromContext.getRole().equals(Role.GROUP_ADMIN) &&
                userFromContext.getBudgetGroupId().equals(userGroupIdFromGivenExpenseId);

        boolean isExpenseOfUser = userFromContext.getExpenseListId().contains(expenseDomainEntity.getId()) &&
                expenseDomainEntity.getUserId().equals(userFromContext.getId());


        if (isExpenseInSameUserGroup && !isExpenseOfUser) {
            throw new ExpenseNotBelongToUserException(userFromContext.getId(), expenseDomainEntity.getId());
        } else if (!isExpenseOfUser) {
            throw new ExpenseNotFoundException(expenseDomainEntity.getId());

        }
    }

    @Override
    public void validateAndSetFieldsForExpense(final ExpenseDtoWriteModel expenseDtoWriteModel,
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

