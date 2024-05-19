package com.patrykdankowski.financeflock.expense;

import jakarta.servlet.http.HttpServletRequest;

public interface ExpenseGeolocationServicePort {

    String getUserIpAddress(HttpServletRequest request);

    String getLocationFromUserIp(String userIp);

    void setLocationForExpenseFromUserIp(final ExpenseDtoWriteModel expenseDtoWriteModel,
                                         final String userIp);

    ExpenseDtoWriteModel validateAndPrepareExpense(ExpenseDtoWriteModel expenseDtoWriteModel,
                                                   String userIp);
}
