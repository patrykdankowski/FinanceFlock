package com.patrykdankowski.financeflock.external_api;

import com.patrykdankowski.financeflock.expense.dto.ExpenseDto;
import jakarta.servlet.http.HttpServletRequest;

 public interface ExpenseGeolocationServicePort {

    String getUserIpAddress(HttpServletRequest request);


    void setLocationForExpenseFromUserIp(final ExpenseDto expenseDto,
                                         final String userIp);

//    ExpenseDtoWriteModel prepareExpense(ExpenseDtoWriteModel expenseDtoWriteModel,
//                                        String userIp);
}
