package com.patrykdankowski.financeflock.expense.port;

import com.patrykdankowski.financeflock.expense.dto.ExpenseDtoWriteModel;
import jakarta.transaction.Transactional;

public interface ExpenseFacade {

    @Transactional
    Long create(ExpenseDtoWriteModel expenseDtoWriteModel, String userIp);

    @Transactional
    void updateExpense(Long id, ExpenseDtoWriteModel expenseSourceDto);

    @Transactional
    void deleteExpense(Long id);
}
