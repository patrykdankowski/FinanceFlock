package com.patrykdankowski.financeflock.expense;

import jakarta.transaction.Transactional;

 interface ExpenseFacade {

    @Transactional
    long addExpense(ExpenseDtoWriteModel expenseDtoWriteModel, String userIp);

    @Transactional
    void updateExpense(Long id, ExpenseDtoWriteModel expenseSourceDto);
}
