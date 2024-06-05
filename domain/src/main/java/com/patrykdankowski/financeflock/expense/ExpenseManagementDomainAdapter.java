package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.budgetgroup.CommonDomainServicePort;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.UserDomainEntity;

import java.time.LocalDateTime;


class ExpenseManagementDomainAdapter implements ExpenseManagementDomainPort {

    private final CommonDomainServicePort commonDomainService;

    ExpenseManagementDomainAdapter(CommonDomainServicePort commonDomainService) {
        this.commonDomainService = commonDomainService;
    }

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
    public void validateUserAccessToExpense(final UserDomainEntity loggedUser,
                                            final UserDomainEntity userFromGivenIdExpense, final ExpenseDomainEntity expenseDomainEntity) {


        boolean isExpenseOfUser = validateIfExpenseOfUser(loggedUser, expenseDomainEntity);
        boolean isExpenseInSameUserGroup = checkIfExpenseIsInSameGroup(loggedUser, userFromGivenIdExpense);


        //for group admin only - different exception
        if (isExpenseInSameUserGroup && !isExpenseOfUser) {
            throw new ExpenseNotBelongToUserException(userFromGivenIdExpense.getId(), expenseDomainEntity.getId());
        } else if (!isExpenseOfUser) {
            throw new ExpenseNotFoundException(expenseDomainEntity.getId());

        }

    }

    private boolean checkIfExpenseIsInSameGroup(UserDomainEntity loggedUser,
                                                UserDomainEntity userFromGivenIdExpense) {
        // TODO fix
        commonDomainService.checkRoleForUser(loggedUser, Role.GROUP_ADMIN);
        commonDomainService.checkIdGroupWithGivenId(userFromGivenIdExpense.getBudgetGroupId(), loggedUser.getBudgetGroupId());

        return true;

    }

    private boolean validateIfExpenseOfUser(UserDomainEntity userFromContext, ExpenseDomainEntity expenseDomainEntity) {
        return userFromContext.getExpenseListId().contains(expenseDomainEntity.getId()) &&
                expenseDomainEntity.getUserId().equals(userFromContext.getId());
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

