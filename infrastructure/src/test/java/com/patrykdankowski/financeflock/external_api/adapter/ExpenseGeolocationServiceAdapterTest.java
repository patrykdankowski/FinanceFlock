package com.patrykdankowski.financeflock.external_api.adapter;


import com.patrykdankowski.financeflock.expense.dto.ApiExpenseDto;
import com.patrykdankowski.financeflock.expense.dto.ExpenseCreateDto;
import com.patrykdankowski.financeflock.expense.exception.ErrorDuringFetchingLocationFromIpException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ExpenseGeolocationServiceAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private ExpenseGeolocationServiceAdapter expenseGeolocationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void getUserIpAddress_shouldReturnForwardedForIp_whenHeaderExists() {
        when(httpServletRequest.getHeader("X-FORWARDED-FOR")).thenReturn("192.168.1.1");

        String ipAddress = expenseGeolocationService.getUserIpAddress(httpServletRequest);

        assertThat(ipAddress).isEqualTo("192.168.1.1");
        verify(httpServletRequest, times(1)).getHeader("X-FORWARDED-FOR");
    }

    @Test
    void getUserIpAddress_shouldReturnRemoteAddr_whenHeaderDoesNotExist() {
        when(httpServletRequest.getHeader("X-FORWARDED-FOR")).thenReturn(null);
        when(httpServletRequest.getRemoteAddr()).thenReturn("10.0.0.1");

        String ipAddress = expenseGeolocationService.getUserIpAddress(httpServletRequest);

        assertThat(ipAddress).isEqualTo("10.0.0.1");
        verify(httpServletRequest, times(1)).getHeader("X-FORWARDED-FOR");
        verify(httpServletRequest, times(1)).getRemoteAddr();
    }

    @Test
    void setLocationForExpenseFromUserIp_shouldSetLocationSuccessfully() {
        String userIp = "192.168.1.1";
        String city = "Warsaw";
        ExpenseCreateDto expenseCreateDto = new ExpenseCreateDto("Test Expense", BigDecimal.valueOf(100), null, LocalDateTime.now());

        ApiExpenseDto apiExpenseDto = new ApiExpenseDto(city);
        ResponseEntity<ApiExpenseDto> responseEntity = new ResponseEntity<>(apiExpenseDto, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), eq(ApiExpenseDto.class))).thenReturn(responseEntity);

        expenseGeolocationService.setLocationForExpenseFromUserIp(expenseCreateDto, userIp);

        assertThat(expenseCreateDto.getLocation()).isEqualTo(city);
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(ApiExpenseDto.class));
    }

    @Test
    void setLocationForExpenseFromUserIp_shouldThrowException_whenApiFails() {
        String userIp = "192.168.1.1";
        ExpenseCreateDto expenseCreateDto = new ExpenseCreateDto("Test Expense", BigDecimal.valueOf(100), null, LocalDateTime.now());

        when(restTemplate.getForEntity(anyString(), eq(ApiExpenseDto.class))).thenThrow(new RuntimeException("API error"));

        assertThatThrownBy(() -> expenseGeolocationService.setLocationForExpenseFromUserIp(expenseCreateDto, userIp))
                .isInstanceOf(ErrorDuringFetchingLocationFromIpException.class);

        verify(restTemplate, times(1)).getForEntity(anyString(), eq(ApiExpenseDto.class));
    }


    @Test
    void setLocationForExpenseFromUserIp_shouldSetUnknownLocation_whenApiReturnsNull() {
        String userIp = "192.168.1.1";
        ExpenseCreateDto expenseCreateDto = new ExpenseCreateDto("Test Expense", BigDecimal.valueOf(100), null, LocalDateTime.now());

        ApiExpenseDto apiExpenseDto = new ApiExpenseDto(null);
        ResponseEntity<ApiExpenseDto> responseEntity = new ResponseEntity<>(apiExpenseDto, HttpStatus.OK);

        when(restTemplate.getForEntity(anyString(), eq(ApiExpenseDto.class))).thenReturn(responseEntity);

        expenseGeolocationService.setLocationForExpenseFromUserIp(expenseCreateDto, userIp);

        assertThat(expenseCreateDto.getLocation()).isEqualTo("Unknown");
        verify(restTemplate, times(1)).getForEntity(anyString(), eq(ApiExpenseDto.class));
    }
}