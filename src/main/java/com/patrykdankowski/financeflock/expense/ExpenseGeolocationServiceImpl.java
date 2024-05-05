package com.patrykdankowski.financeflock.expense;

import com.patrykdankowski.financeflock.expense.dto.ExpenseGeolocationDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
class ExpenseGeolocationServiceImpl implements ExpenseGeolocationService {

    ExpenseGeolocationServiceImpl(final RestTemplate restTemplate) {
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
        ResponseEntity<ExpenseGeolocationDto> response = restTemplate.getForEntity(apiUrl, ExpenseGeolocationDto.class);

        ExpenseGeolocationDto geolocationDto = response.getBody();
        if (geolocationDto != null && geolocationDto.getCity() != null) {
            return geolocationDto.getCity();
        } else {
            return "Unknown";
        }
    }
}
