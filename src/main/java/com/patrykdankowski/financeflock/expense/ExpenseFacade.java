package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.expense.dto.ExpenseDtoWriteModel;
import jakarta.transaction.Transactional;

public interface ExpenseFacade {

    @Transactional
    long addExpense(ExpenseDtoWriteModel expenseDtoWriteModel, String userIp);

    @Transactional
    void updateExpense(Long id, ExpenseDtoWriteModel expenseSourceDto);
}
