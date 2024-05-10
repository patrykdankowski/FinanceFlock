package com.patrykdankowski.financeflock.geolocationAPI;

import com.patrykdankowski.financeflock.expense.dto.ExpenseDtoWriteModel;
import jakarta.servlet.http.HttpServletRequest;

public interface ExpenseGeolocationService {

    String getUserIpAddress(HttpServletRequest request);

    String getLocationFromUserIp(String userIp);

    void setLocationForExpenseFromUserIp(final ExpenseDtoWriteModel expenseDtoWriteModel,
                                         final String userIp);
}
