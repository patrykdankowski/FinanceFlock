package com.patrykdankowski.financeflock.external_api;

import com.patrykdankowski.financeflock.expense.dto.ApiExpenseDto;
import com.patrykdankowski.financeflock.expense.dto.ExpenseCreateDto;
import com.patrykdankowski.financeflock.expense.exception.ErrorDuringFetchingLocationFromIpException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
class ExpenseGeolocationServiceAdapter implements ExpenseGeolocationServicePort {

    ExpenseGeolocationServiceAdapter(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final RestTemplate restTemplate;

    @Value("${FinanceFlock-api-key}")
    private String apiKey;

    @Override
    public String getUserIpAddress(HttpServletRequest request) {
        String remoteAddress = "";
        if (request != null) {
            remoteAddress = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddress == null || remoteAddress.equals("")) {
                remoteAddress = request.getRemoteAddr();
            }
        }
        return remoteAddress;

    }

    @Override
    public void setLocationForExpenseFromUserIp(final ExpenseCreateDto expenseCreateDto,
                                                final String userIp) {
        try {
            String city = getLocationFromUserIp(userIp);
            expenseCreateDto.setLocation(city);
        } catch (Exception e) {
            throw new ErrorDuringFetchingLocationFromIpException();
        }
    }

    private String getLocationFromUserIp(String userIp) {
        String apiUrl = "https://ipinfo.io/" + userIp + "/json?token=" + apiKey;
        ResponseEntity<ApiExpenseDto> response = restTemplate.getForEntity(apiUrl, ApiExpenseDto.class);

        ApiExpenseDto geolocationDto = response.getBody();
        if (geolocationDto != null && geolocationDto.getCity() != null) {
            return geolocationDto.getCity();
        } else {
            return "Unknown";
        }
    }
//    @Override
//    public ExpenseDtoWriteModel prepareExpense(final ExpenseDtoWriteModel expenseDtoWriteModel,
//                                               final String userIp) {
//        if (expenseDtoWriteModel.getLocation() == null || expenseDtoWriteModel.getLocation().isEmpty()) {
//            setLocationForExpenseFromUserIp(expenseDtoWriteModel, userIp);
//
//        }
//        return expenseDtoWriteModel;
//
//
//    }
}
