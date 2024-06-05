package com.patrykdankowski.financeflock.expense;

import jakarta.transaction.Transactional;

 interface ExpenseFacade {

    @Transactional
    Long addExpense(ExpenseDtoWriteModel expenseDtoWriteModel, String userIp);

    @Transactional
    void updateExpense(Long id, ExpenseDtoWriteModel expenseSourceDto);
}
