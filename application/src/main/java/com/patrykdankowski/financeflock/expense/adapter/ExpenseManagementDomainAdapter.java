package com.patrykdankowski.financeflock.expense.adapter;

import com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity;
import com.patrykdankowski.financeflock.expense.model.vo.ExpenseUpdateVO;
import com.patrykdankowski.financeflock.expense.port.ExpenseFactoryPort;
import com.patrykdankowski.financeflock.expense.port.ExpenseManagementDomainPort;
import com.patrykdankowski.financeflock.expense.model.vo.ExpenseCreateVO;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import org.springframework.stereotype.Service;

@Service
class ExpenseManagementDomainAdapter implements ExpenseManagementDomainPort {

    private final ExpenseFactoryPort expenseFactory;

    ExpenseManagementDomainAdapter(final ExpenseFactoryPort expenseFactory) {
        this.expenseFactory = expenseFactory;
    }


    @Override
    public ExpenseDomainEntity createExpense(final ExpenseCreateVO expenseCreateVO,
                                             final UserDomainEntity userFromContext) {
        return expenseFactory.createExpanseFromRequest(
                null,
                userFromContext.getId(),
                expenseCreateVO.amountVO().value(),
                expenseCreateVO.expenseDate(),
                expenseCreateVO.description(),
                expenseCreateVO.location());

//        return buildExpense(null,
//                userFromContext.getId(),
//                expenseValueObject.getAmount(),
//                expenseValueObject.getExpenseDate(),
//                expenseValueObject.getDescription(),
//                expenseValueObject.getLocation());

    }

    @Override
    public void updateExpense(final ExpenseUpdateVO expenseUpdateVO,
                              final ExpenseDomainEntity expenseDomainEntity) {
        expenseDomainEntity.updateInfo(expenseUpdateVO.amount(),
                expenseUpdateVO.expenseDate(),
                expenseUpdateVO.description(),
                expenseUpdateVO.location());
    }

}

