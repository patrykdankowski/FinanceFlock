package com.patrykdankowski.financeflock.expense.adapter;

import com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity;
import com.patrykdankowski.financeflock.expense.port.ExpenseFactoryPort;
import com.patrykdankowski.financeflock.expense.port.ExpenseManagementDomainPort;
import com.patrykdankowski.financeflock.expense.model.vo.ExpenseValueObject;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import org.springframework.stereotype.Service;

import static com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity.buildExpense;

@Service
class ExpenseManagementDomainAdapter implements ExpenseManagementDomainPort {

    private final ExpenseFactoryPort expenseFactory;

    ExpenseManagementDomainAdapter(final ExpenseFactoryPort expenseFactory) {
        this.expenseFactory = expenseFactory;
    }


    @Override
    public ExpenseDomainEntity createExpense(final ExpenseValueObject expenseValueObject,
                                             final UserDomainEntity userFromContext) {
        return expenseFactory.createExpanseFromRequest(
                null,
                userFromContext.getId(),
                expenseValueObject.getAmount(),
                expenseValueObject.getExpenseDate(),
                expenseValueObject.getDescription(),
                expenseValueObject.getLocation());

//        return buildExpense(null,
//                userFromContext.getId(),
//                expenseValueObject.getAmount(),
//                expenseValueObject.getExpenseDate(),
//                expenseValueObject.getDescription(),
//                expenseValueObject.getLocation());

    }

    @Override
    public void updateExpense(final ExpenseValueObject expenseValueObject,
                              final ExpenseDomainEntity expenseDomainEntity) {
        expenseDomainEntity.updateInfo(expenseValueObject.getAmount(),
                expenseValueObject.getExpenseDate(),
                expenseValueObject.getDescription(),
                expenseValueObject.getLocation());
    }

}

