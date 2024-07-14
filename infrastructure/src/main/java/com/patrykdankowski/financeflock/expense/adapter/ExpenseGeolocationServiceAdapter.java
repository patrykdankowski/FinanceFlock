package com.patrykdankowski.financeflock.expense.adapter;

import com.patrykdankowski.financeflock.expense.dto.ApiExpenseDto;
import com.patrykdankowski.financeflock.expense.dto.ExpenseDtoWriteModel;
import com.patrykdankowski.financeflock.expense.port.ExpenseGeolocationServicePort;
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
    public String getLocationFromUserIp(String userIp) {
        String apiUrl = "https://ipinfo.io/" + userIp + "/json?token=" + apiKey;
        ResponseEntity<ApiExpenseDto> response = restTemplate.getForEntity(apiUrl, ApiExpenseDto.class);

        ApiExpenseDto geolocationDto = response.getBody();
        if (geolocationDto != null && geolocationDto.getCity() != null) {
            return geolocationDto.getCity();
        } else {
            return "Unknown";
        }
    }
    @Override
    public void setLocationForExpenseFromUserIp(final ExpenseDtoWriteModel expenseDtoWriteModel,
                                                final String userIp) {
        try {
            String city = getLocationFromUserIp(userIp);
            expenseDtoWriteModel.setLocation(city);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @Override
    public ExpenseDtoWriteModel prepareExpense(final ExpenseDtoWriteModel expenseDtoWriteModel,
                                               final String userIp) {
        if (expenseDtoWriteModel.getLocation() == null || expenseDtoWriteModel.getLocation().isEmpty()) {
            setLocationForExpenseFromUserIp(expenseDtoWriteModel, userIp);

        }
        return expenseDtoWriteModel;


    }
}
