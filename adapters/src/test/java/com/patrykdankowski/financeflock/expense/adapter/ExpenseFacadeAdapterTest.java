package com.patrykdankowski.financeflock.expense.adapter;

import com.patrykdankowski.financeflock.auth.port.AuthenticationServicePort;
import com.patrykdankowski.financeflock.expense.dto.ExpenseCreateDto;
import com.patrykdankowski.financeflock.expense.dto.ExpenseUpdateDto;
import com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity;
import com.patrykdankowski.financeflock.expense.port.ExpenseCommandServicePort;
import com.patrykdankowski.financeflock.expense.port.ExpenseManagementDomainPort;
import com.patrykdankowski.financeflock.expense.port.ExpenseValidatorPort;
import com.patrykdankowski.financeflock.external_api.ExpenseGeolocationServicePort;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.port.UserCommandServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ExpenseFacadeAdapterTest {

    @Mock
    private ExpenseManagementDomainPort expenseManagementDomain;

    @Mock
    private AuthenticationServicePort authenticationService;

    @Mock
    private ExpenseCommandServicePort expenseCommandService;

    @Mock
    private ExpenseGeolocationServicePort expenseGeolocationService;

    @Mock
    private UserCommandServicePort userCommandService;

    @Mock
    private ExpenseValidatorPort expenseValidator;

    @InjectMocks
    private ExpenseFacadeAdapter expenseFacadeAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createExpense_shouldThrowException_whenUserNotAuthenticated() {
        ExpenseCreateDto expenseCreateDto = new ExpenseCreateDto("Test Expense", BigDecimal.valueOf(100.0), "Test Location", LocalDateTime.now());

        when(authenticationService.getFullUserFromContext()).thenThrow(new SecurityException("User not authenticated"));

        assertThatThrownBy(() -> expenseFacadeAdapter.createExpense(expenseCreateDto, "11.18.15.33"))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("User not authenticated");

        verify(expenseCommandService, never()).saveExpense(any());
        verify(userCommandService, never()).saveUser(any());
    }

    @Test
    void createExpense_shouldThrowException_whenExpenseCreationFails() {
        ExpenseCreateDto expenseCreateDto = new ExpenseCreateDto("Test Expense", BigDecimal.valueOf(100.0), "Test Location", LocalDateTime.now());

        UserDomainEntity loggedUser = mock(UserDomainEntity.class);
        when(authenticationService.getFullUserFromContext()).thenReturn(loggedUser);
        when(expenseManagementDomain.createExpense(any(), any())).thenThrow(new RuntimeException("Expense creation failed"));

        assertThatThrownBy(() -> expenseFacadeAdapter.createExpense(expenseCreateDto, "192.168.0.1"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Expense creation failed");

        verify(expenseCommandService, never()).saveExpense(any());
        verify(userCommandService, never()).saveUser(any());
    }

    @Test
    void updateExpense_shouldThrowException_whenExpenseDoesNotExist() {
        Long expenseId = 1L;
        ExpenseUpdateDto expenseUpdateDto = new ExpenseUpdateDto("Updated Expense", BigDecimal.valueOf(150.0), "Updated Location", LocalDateTime.now());

        UserDomainEntity loggedUser = mock(UserDomainEntity.class);
        when(authenticationService.getFullUserFromContext()).thenReturn(loggedUser);
        when(expenseCommandService.findExpenseById(expenseId)).thenThrow(new RuntimeException("Expense not found"));

        assertThatThrownBy(() -> expenseFacadeAdapter.updateExpense(expenseId, expenseUpdateDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Expense not found");

        verify(expenseValidator, never()).validateAccessToExpense(any(), any());
        verify(expenseCommandService, never()).saveExpense(any());
    }

    @Test
    void deleteExpense_shouldThrowException_whenUserHasNoAccess() {
        Long expenseId = 1L;

        UserDomainEntity loggedUser = mock(UserDomainEntity.class);
        ExpenseDomainEntity expenseToDelete = mock(ExpenseDomainEntity.class);
        when(authenticationService.getFullUserFromContext()).thenReturn(loggedUser);
        when(expenseCommandService.findExpenseById(expenseId)).thenReturn(expenseToDelete);
        doThrow(new SecurityException("Access Denied")).when(expenseValidator).validateAccessToExpense(loggedUser, expenseToDelete);

        assertThatThrownBy(() -> expenseFacadeAdapter.deleteExpense(expenseId))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("Access Denied");

        verify(userCommandService, never()).saveUser(loggedUser);
        verify(expenseCommandService, never()).deleteExpense(expenseId);
    }

    @Test
    void createExpense_shouldThrowException_whenGeolocationServiceFails() {
        ExpenseCreateDto expenseCreateDto = new ExpenseCreateDto("Test Expense", BigDecimal.valueOf(100.0), null, LocalDateTime.now());

        UserDomainEntity loggedUser = mock(UserDomainEntity.class);
        when(authenticationService.getFullUserFromContext()).thenReturn(loggedUser);
        doThrow(new RuntimeException("Geolocation service failed")).when(expenseGeolocationService).setLocationForExpenseFromUserIp(any(), any());

        assertThatThrownBy(() -> expenseFacadeAdapter.createExpense(expenseCreateDto, "192.168.0.1"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Geolocation service failed");

        verify(expenseCommandService, never()).saveExpense(any());
        verify(userCommandService, never()).saveUser(any());
    }

    @Test
    void deleteExpense_shouldThrowException_whenExpenseDoesNotExist() {
        Long expenseId = 1L;

        UserDomainEntity loggedUser = mock(UserDomainEntity.class);
        when(authenticationService.getFullUserFromContext()).thenReturn(loggedUser);
        when(expenseCommandService.findExpenseById(expenseId)).thenThrow(new RuntimeException("Expense not found"));

        assertThatThrownBy(() -> expenseFacadeAdapter.deleteExpense(expenseId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Expense not found");

        verify(expenseValidator, never()).validateAccessToExpense(any(), any());
        verify(userCommandService, never()).saveUser(any());
        verify(expenseCommandService, never()).deleteExpense(anyLong());
    }

    @Test
    void updateExpense_shouldThrowException_whenExpenseUpdateFails() {
        Long expenseId = 1L;
        ExpenseUpdateDto expenseUpdateDto = new ExpenseUpdateDto("Updated Expense", BigDecimal.valueOf(150.0), "Updated Location", LocalDateTime.now());

        UserDomainEntity loggedUser = mock(UserDomainEntity.class);
        ExpenseDomainEntity existingExpense = mock(ExpenseDomainEntity.class);
        when(authenticationService.getFullUserFromContext()).thenReturn(loggedUser);
        when(expenseCommandService.findExpenseById(expenseId)).thenReturn(existingExpense);
        doThrow(new RuntimeException("Failed to update expense")).when(expenseManagementDomain).updateExpense(any(), any());

        assertThatThrownBy(() -> expenseFacadeAdapter.updateExpense(expenseId, expenseUpdateDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to update expense");

        verify(expenseCommandService, never()).saveExpense(any());
    }

    @Test
    void deleteExpense_shouldDeleteExpenseSuccessfully_whenValidDataProvided() {
        Long expenseId = 1L;

        UserDomainEntity loggedUser = mock(UserDomainEntity.class);
        when(authenticationService.getFullUserFromContext()).thenReturn(loggedUser);

        ExpenseDomainEntity expenseToDelete = mock(ExpenseDomainEntity.class);
        when(expenseCommandService.findExpenseById(expenseId)).thenReturn(expenseToDelete);

        expenseFacadeAdapter.deleteExpense(expenseId);

        verify(expenseValidator, times(1)).validateAccessToExpense(loggedUser, expenseToDelete);
        verify(userCommandService, times(1)).saveUser(loggedUser);
        verify(expenseCommandService, times(1)).deleteExpense(expenseId);
    }
}
