package com.patrykdankowski.financeflock.expense.port;

import com.patrykdankowski.financeflock.expense.dto.ExpenseDtoWriteModel;
import jakarta.servlet.http.HttpServletRequest;

 public interface ExpenseGeolocationServicePort {

    String getUserIpAddress(HttpServletRequest request);

    String getLocationFromUserIp(String userIp);

    void setLocationForExpenseFromUserIp(final ExpenseDtoWriteModel expenseDtoWriteModel,
                                         final String userIp);

    ExpenseDtoWriteModel prepareExpense(ExpenseDtoWriteModel expenseDtoWriteModel,
                                        String userIp);
}
