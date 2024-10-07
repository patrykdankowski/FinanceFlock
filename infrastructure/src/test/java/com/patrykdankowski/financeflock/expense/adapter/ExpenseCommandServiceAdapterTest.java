package com.patrykdankowski.financeflock.expense.adapter;

import com.patrykdankowski.financeflock.expense.exception.ExpenseNotFoundException;
import com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity;
import com.patrykdankowski.financeflock.expense.port.ExpenseCommandRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ExpenseCommandServiceAdapterTest {

    @Mock
    private ExpenseCommandRepositoryPort expenseCommandRepository;

    @InjectMocks
    private ExpenseCommandServiceAdapter expenseCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void findExpenseById_shouldReturnExpense_whenExpenseExists() {
        Long expenseId = 1L;
        Long userId = 100L;
        BigDecimal amount = BigDecimal.valueOf(50.0);
        LocalDateTime expenseDate = LocalDateTime.now();
        String description = "Groceries";
        String location = "Supermarket";

        ExpenseDomainEntity expense = createExpense(expenseId, userId, amount, expenseDate, description, location);

        when(expenseCommandRepository.findById(expenseId)).thenReturn(Optional.of(expense));

        ExpenseDomainEntity result = expenseCommandService.findExpenseById(expenseId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(expenseId);
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getLocation()).isEqualTo(location);
        verify(expenseCommandRepository, times(1)).findById(expenseId);
    }

    @Test
    void findExpenseById_shouldThrowExpenseNotFoundException_whenExpenseDoesNotExist() {
        Long expenseId = 1L;

        when(expenseCommandRepository.findById(expenseId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> expenseCommandService.findExpenseById(expenseId))
                .isInstanceOf(ExpenseNotFoundException.class)
                .hasMessageContaining("Expense  with ID " + expenseId + " not found");

        verify(expenseCommandRepository, times(1)).findById(expenseId);
    }


    @Test
    void saveExpense_shouldSaveAndReturnExpense() {
        Long expenseId = 1L;
        Long userId = 100L;
        BigDecimal amount = BigDecimal.valueOf(50.0);
        LocalDateTime expenseDate = LocalDateTime.now();
        String description = "Groceries";
        String location = "Supermarket";

        ExpenseDomainEntity expense = createExpense(expenseId, userId, amount, expenseDate, description, location);

        when(expenseCommandRepository.save(expense)).thenReturn(expense);

        ExpenseDomainEntity result = expenseCommandService.saveExpense(expense);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(expenseId);
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getLocation()).isEqualTo(location);
        verify(expenseCommandRepository, times(1)).save(expense);
    }


    @Test
    void deleteExpense_shouldDeleteExpenseById() {
        Long expenseId = 1L;

        doNothing().when(expenseCommandRepository).deleteById(expenseId);

        expenseCommandService.deleteExpense(expenseId);

        verify(expenseCommandRepository, times(1)).deleteById(expenseId);
    }

    @Test
    void deleteExpense_shouldDoNothing_whenExpenseDoesNotExist() {
        Long expenseId = 1L;

        doNothing().when(expenseCommandRepository).deleteById(expenseId);

        expenseCommandService.deleteExpense(expenseId);

        verify(expenseCommandRepository, times(1)).deleteById(expenseId);
    }


    private ExpenseDomainEntity createExpense(Long id, Long userId, BigDecimal amount, LocalDateTime expenseDate, String description, String location) {
        return ExpenseDomainEntity.buildExpense(id, userId, amount, expenseDate, description, location);
    }
}
