package com.patrykdankowski.financeflock.expense;

import jakarta.servlet.http.HttpServletRequest;

public interface ExpenseGeolocationService {

    String getUserIpAddress(HttpServletRequest request);

    String getLocationFromUserIp(String userIp);
}
