package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.common.CommonDomainServicePort;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.UserDomainEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;


class ExpenseManagementDomainAdapter implements ExpenseManagementDomainPort {

    private static final Logger log = LoggerFactory.getLogger(ExpenseManagementDomainAdapter.class);
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
        if (isExpenseInSameUserGroup && !isExpenseOfUser && isAdmin(loggedUser)) {
            throw new ExpenseNotBelongToUserException(userFromGivenIdExpense.getId(), expenseDomainEntity.getId());
        } else if (!isExpenseOfUser) {
            throw new ExpenseNotFoundException(expenseDomainEntity.getId());

        }

    }

    private boolean isAdmin(UserDomainEntity loggedUser) {
        return loggedUser.getRole().equals(Role.GROUP_ADMIN);
    }

    private boolean checkIfExpenseIsInSameGroup(UserDomainEntity loggedUser,
                                                UserDomainEntity userFromGivenIdExpense) {
        if (loggedUser.getBudgetGroupId() == null) {
            return false;
        }
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
        expenseDomainEntity.updateInfo(expenseDtoWriteModel.getAmount(),
                expenseDtoWriteModel.getExpenseDate(),
                expenseDtoWriteModel.getDescription(),
                expenseDtoWriteModel.getLocation());
    }
}

