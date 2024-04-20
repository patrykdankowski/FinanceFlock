package com.patrykdankowski.financeflock.expense;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
class ExpenseGeolocationService {

    ExpenseGeolocationService(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final RestTemplate restTemplate;

    @Value("${FinanceFlock-api-key}")
    private String apiKey;


    String getUserIpAddress(HttpServletRequest request) {
        String remoteAddress = "";
        if (request != null) {
            remoteAddress = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddress == null || remoteAddress.equals("")) {
                remoteAddress = request.getRemoteAddr();
            }
        }
        return remoteAddress;

    }

    String getLocationFromUserIp(String userIp) {
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
