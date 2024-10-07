package com.patrykdankowski.financeflock.expense.adapter;

import com.patrykdankowski.financeflock.expense.dto.ExpenseCreateDto;
import com.patrykdankowski.financeflock.expense.dto.ExpenseUpdateDto;
import com.patrykdankowski.financeflock.expense.port.ExpenseFacadePort;
import com.patrykdankowski.financeflock.external_api.ExpenseGeolocationServicePort;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ExpenseControllerAdapterTest {

    @Mock
    private ExpenseFacadePort expenseFacade;

    @Mock
    private ExpenseGeolocationServicePort geolocationService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private ExpenseControllerAdapter expenseControllerAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void addExpense_shouldReturnSuccessMessage_whenCreationIsSuccessful() {
        ExpenseCreateDto expenseCreateDto = new ExpenseCreateDto("Test Expense", BigDecimal.valueOf(100.0), "Test Location", LocalDateTime.now());
        String userIp = "192.168.1.1";
        long expenseId = 1L;

        when(geolocationService.getUserIpAddress(request)).thenReturn(userIp);
        when(expenseFacade.createExpense(expenseCreateDto, userIp)).thenReturn(expenseId);

        String response = expenseControllerAdapter.addExpense(expenseCreateDto, request);

        assertThat(response).isEqualTo("Expense created with id 1");
        verify(geolocationService, times(1)).getUserIpAddress(request);
        verify(expenseFacade, times(1)).createExpense(expenseCreateDto, userIp);
    }

    @Test
    void updateExpense_shouldReturnSuccessMessage_whenUpdateIsSuccessful() {
        Long expenseId = 1L;
        ExpenseUpdateDto expenseUpdateDto = new ExpenseUpdateDto("Updated Expense", BigDecimal.valueOf(300L), "Updated Location", LocalDateTime.now());

        doNothing().when(expenseFacade).updateExpense(expenseId, expenseUpdateDto);

        String response = expenseControllerAdapter.updateExpense(expenseId, expenseUpdateDto);

        assertThat(response).isEqualTo("Resource updated");
        verify(expenseFacade, times(1)).updateExpense(expenseId, expenseUpdateDto);
    }

    @Test
    void deleteExpense_shouldCallFacadeToDeleteExpense() {
        Long expenseId = 1L;

        doNothing().when(expenseFacade).deleteExpense(expenseId);

        expenseControllerAdapter.deleteExpense(expenseId);

        verify(expenseFacade, times(1)).deleteExpense(expenseId);
    }


    @Test
    void addExpense_shouldThrowException_whenCreateExpenseFails() {
        ExpenseCreateDto expenseCreateDto = new ExpenseCreateDto("Test Expense", BigDecimal.valueOf(100.0), "Test Location", LocalDateTime.now());
        String userIp = "192.168.1.1";

        when(geolocationService.getUserIpAddress(request)).thenReturn(userIp);
        doThrow(new RuntimeException("Failed to create expense"))
                .when(expenseFacade).createExpense(expenseCreateDto, userIp);

        assertThatThrownBy(() -> expenseControllerAdapter.addExpense(expenseCreateDto, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to create expense");

        verify(geolocationService, times(1)).getUserIpAddress(request);
        verify(expenseFacade, times(1)).createExpense(expenseCreateDto, userIp);
    }

    @Test
    void updateExpense_shouldThrowConstraintViolationException_whenInvalidUpdateDataProvided() {
        Long expenseId = 1L;
        ExpenseUpdateDto invalidExpenseUpdateDto = new ExpenseUpdateDto(null, null, null, null);

        doThrow(new ConstraintViolationException("Validation failed", null))
                .when(expenseFacade).updateExpense(expenseId, invalidExpenseUpdateDto);

        assertThatThrownBy(() -> expenseControllerAdapter.updateExpense(expenseId, invalidExpenseUpdateDto))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("Validation failed");

        verify(expenseFacade, times(1)).updateExpense(expenseId, invalidExpenseUpdateDto);
    }

    @Test
    void deleteExpense_shouldThrowException_whenExpenseDoesNotExist() {
        Long expenseId = 1L;

        doThrow(new RuntimeException("Expense not found"))
                .when(expenseFacade).deleteExpense(expenseId);

        assertThatThrownBy(() -> expenseControllerAdapter.deleteExpense(expenseId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Expense not found");

        verify(expenseFacade, times(1)).deleteExpense(expenseId);
    }

    @Test
    void addExpense_shouldThrowException_whenGeolocationServiceFails() {
        ExpenseCreateDto expenseCreateDto = new ExpenseCreateDto("Test Expense", BigDecimal.valueOf(100.0), "Test Location", LocalDateTime.now());
        when(geolocationService.getUserIpAddress(request)).thenThrow(new RuntimeException("Unable to retrieve IP"));

        assertThatThrownBy(() -> expenseControllerAdapter.addExpense(expenseCreateDto, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Unable to retrieve IP");

        verify(geolocationService, times(1)).getUserIpAddress(request);
        verify(expenseFacade, never()).createExpense(any(), any());
    }

    @Test
    void addExpense_shouldThrowConstraintViolationException_whenInvalidCreateDataProvided() {
        ExpenseCreateDto invalidExpenseCreateDto = new ExpenseCreateDto(null, null, null, null);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<ExpenseCreateDto>> violations = validator.validate(invalidExpenseCreateDto);

        assertThat(violations).isNotEmpty();
    }

    @Test
    void updateExpense_shouldThrowException_whenInvalidExpenseIdProvided() {
        Long invalidExpenseId = -1L;
        ExpenseUpdateDto expenseUpdateDto = new ExpenseUpdateDto("Updated Expense", BigDecimal.valueOf(150.0), "Updated Location", LocalDateTime.now());

        doThrow(new RuntimeException("Invalid Expense ID"))
                .when(expenseFacade).updateExpense(eq(invalidExpenseId), any());

        assertThatThrownBy(() -> expenseControllerAdapter.updateExpense(invalidExpenseId, expenseUpdateDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Invalid Expense ID");

        verify(expenseFacade, times(1)).updateExpense(eq(invalidExpenseId), any());
    }

    @Test
    void addExpense_shouldThrowException_whenGeolocationServiceIsUnavailable() {
        ExpenseCreateDto expenseCreateDto = new ExpenseCreateDto("Test Expense", BigDecimal.valueOf(100.0), "Test Location", LocalDateTime.now());
        when(geolocationService.getUserIpAddress(request))
                .thenThrow(new RuntimeException("Geolocation service unavailable"));

        assertThatThrownBy(() -> expenseControllerAdapter.addExpense(expenseCreateDto, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Geolocation service unavailable");

        verify(geolocationService, times(1)).getUserIpAddress(request);
        verify(expenseFacade, never()).createExpense(any(), any());
    }

    @Test
    void updateExpense_shouldNotUpdateExpense_whenNullValuesProvided() {
        Long expenseId = 1L;
        ExpenseUpdateDto invalidExpenseUpdateDto = new ExpenseUpdateDto(null, null, null, null);

        doNothing().when(expenseFacade).updateExpense(expenseId, invalidExpenseUpdateDto);

        String response = expenseControllerAdapter.updateExpense(expenseId, invalidExpenseUpdateDto);

        assertThat(response).isEqualTo("Resource updated");
        verify(expenseFacade, times(1)).updateExpense(expenseId, invalidExpenseUpdateDto);
    }

    @Test
    void addExpense_shouldCallGeolocationServiceOnce_whenCreatingExpense() {
        ExpenseCreateDto expenseCreateDto = new ExpenseCreateDto("Test Expense", BigDecimal.valueOf(100.0), "Test Location", LocalDateTime.now());
        String userIp = "192.168.1.1";
        long expenseId = 1L;

        when(geolocationService.getUserIpAddress(request)).thenReturn(userIp);
        when(expenseFacade.createExpense(expenseCreateDto, userIp)).thenReturn(expenseId);

        String response = expenseControllerAdapter.addExpense(expenseCreateDto, request);

        assertThat(response).isEqualTo("Expense created with id 1");
        verify(geolocationService, times(1)).getUserIpAddress(request);
        verify(expenseFacade, times(1)).createExpense(expenseCreateDto, userIp);
    }

    @Test
    void updateExpense_shouldThrowException_whenFacadeThrowsUnexpectedException() {
        Long expenseId = 1L;
        ExpenseUpdateDto expenseUpdateDto = new ExpenseUpdateDto("Update Name", BigDecimal.valueOf(100.00), "Test Location", LocalDateTime.now());

        doThrow(new RuntimeException("Unexpected error"))
                .when(expenseFacade).updateExpense(expenseId, expenseUpdateDto);

        assertThatThrownBy(() -> expenseControllerAdapter.updateExpense(expenseId, expenseUpdateDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Unexpected error");

        verify(expenseFacade, times(1)).updateExpense(expenseId, expenseUpdateDto);
    }

    @Test
    void deleteExpense_shouldNotThrowException_whenExpenseDoesNotExistAndIgnored() {
        Long expenseId = 1L;

        doNothing().when(expenseFacade).deleteExpense(expenseId);

        expenseControllerAdapter.deleteExpense(expenseId);

        verify(expenseFacade, times(1)).deleteExpense(expenseId);
    }


    @Test
    void updateExpense_shouldHandlePartialUpdatesCorrectly() {
        Long expenseId = 1L;
        ExpenseUpdateDto partialUpdateDto = new ExpenseUpdateDto("Partially Updated", null, null, null);

        doNothing().when(expenseFacade).updateExpense(expenseId, partialUpdateDto);

        String response = expenseControllerAdapter.updateExpense(expenseId, partialUpdateDto);

        assertThat(response).isEqualTo("Resource updated");
        verify(expenseFacade, times(1)).updateExpense(expenseId, partialUpdateDto);
    }
}
