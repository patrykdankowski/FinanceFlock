package com.patrykdankowski.financeflock.external_api;

import com.patrykdankowski.financeflock.expense.dto.ExpenseCreateDto;
import jakarta.servlet.http.HttpServletRequest;

 public interface ExpenseGeolocationServicePort {

    String getUserIpAddress(HttpServletRequest request);


    void setLocationForExpenseFromUserIp(final ExpenseCreateDto expenseCreateDto,
                                         final String userIp);
}
