package com.patrykdankowski.financeflock.expense.adapter;
import com.patrykdankowski.financeflock.expense.model.entity.ExpenseDomainEntity;
import com.patrykdankowski.financeflock.expense.model.vo.AmountVO;
import com.patrykdankowski.financeflock.expense.model.vo.ExpenseCreateVO;
import com.patrykdankowski.financeflock.expense.model.vo.ExpenseUpdateVO;
import com.patrykdankowski.financeflock.expense.port.ExpenseFactoryPort;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;



class ExpenseManagementDomainAdapterTest {

    @Mock
    private ExpenseFactoryPort expenseFactory;

    @InjectMocks
    private ExpenseManagementDomainAdapter expenseManagementDomainAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createExpense_shouldReturnExpenseDomainEntity() {
        // given
        Long userId = 100L;
        UserDomainEntity user = UserDomainEntity.buildUser(userId, "User1", "password", "user1@example.com", LocalDateTime.now());
        ExpenseCreateVO expenseCreateVO = new ExpenseCreateVO("Description",new AmountVO(new BigDecimal(100.00)),"Location", LocalDateTime.now());
        ExpenseDomainEntity expectedExpense = ExpenseDomainEntity.buildExpense(null, userId, expenseCreateVO.amountVO().value(), expenseCreateVO.expenseDate(), expenseCreateVO.description(), expenseCreateVO.location());
        when(expenseFactory.createExpanseFromRequest(null, userId, expenseCreateVO.amountVO().value(), expenseCreateVO.expenseDate(), expenseCreateVO.description(), expenseCreateVO.location()))
                .thenReturn(expectedExpense);

        // when
        ExpenseDomainEntity createdExpense = expenseManagementDomainAdapter.createExpense(expenseCreateVO, user);

        // then
        assertThat(createdExpense).isNotNull();
        assertThat(createdExpense.getUserId()).isEqualTo(userId);
        assertThat(createdExpense.getAmount()).isEqualByComparingTo(expenseCreateVO.amountVO().value());
        assertThat(createdExpense.getDescription()).isEqualTo(expenseCreateVO.description());
        verify(expenseFactory, times(1)).createExpanseFromRequest(null, userId, expenseCreateVO.amountVO().value(), expenseCreateVO.expenseDate(), expenseCreateVO.description(), expenseCreateVO.location());
    }

    @Test
    void updateExpense_shouldUpdateExpenseDomainEntity() {
        // given
        ExpenseDomainEntity existingExpense = ExpenseDomainEntity.buildExpense(1L, 100L, new BigDecimal("50.00"), LocalDateTime.now(), "Old Description", "Old Location");
        ExpenseUpdateVO expenseUpdateVO = new ExpenseUpdateVO("New Description", new BigDecimal("150.00"), "New Location", LocalDateTime.now().plusDays(1));

        // when
        expenseManagementDomainAdapter.updateExpense(expenseUpdateVO, existingExpense);

        // then
        assertThat(existingExpense.getAmount()).isEqualByComparingTo(expenseUpdateVO.amount());
        assertThat(existingExpense.getDescription()).isEqualTo(expenseUpdateVO.description());
        assertThat(existingExpense.getExpenseDate()).isEqualTo(expenseUpdateVO.expenseDate());
        assertThat(existingExpense.getLocation()).isEqualTo(expenseUpdateVO.location());
    }

    @Test
    void updateExpense_shouldNotUpdateNullFields() {
        // given
        BigDecimal originalAmount = new BigDecimal("75.00");
        LocalDateTime originalDate = LocalDateTime.now();
        String originalDescription = "Original Description";
        String originalLocation = "Original Location";

        ExpenseDomainEntity existingExpense = ExpenseDomainEntity.buildExpense(1L, 100L, originalAmount, originalDate, originalDescription, originalLocation);

        ExpenseUpdateVO expenseUpdateVO = new ExpenseUpdateVO(null, null, null, null);

        // when
        expenseManagementDomainAdapter.updateExpense(expenseUpdateVO, existingExpense);

        // then
        assertThat(existingExpense.getAmount()).isEqualByComparingTo(originalAmount);
        assertThat(existingExpense.getExpenseDate()).isEqualTo(originalDate);
        assertThat(existingExpense.getDescription()).isEqualTo(originalDescription);
        assertThat(existingExpense.getLocation()).isEqualTo(originalLocation);
    }

    @Test
    void updateExpense_shouldUpdateOnlyNonNullFields() {
        // given
        BigDecimal originalAmount = new BigDecimal("75.00");
        LocalDateTime originalDate = LocalDateTime.now();
        String originalDescription = "Original Description";
        String originalLocation = "Original Location";

        ExpenseDomainEntity existingExpense = ExpenseDomainEntity.buildExpense(1L, 100L, originalAmount, originalDate, originalDescription, originalLocation);

        ExpenseUpdateVO expenseUpdateVO = new ExpenseUpdateVO("Updated Description", new BigDecimal("200.00"), null, null);

        // when
        expenseManagementDomainAdapter.updateExpense(expenseUpdateVO, existingExpense);

        // then
        assertThat(existingExpense.getAmount()).isEqualByComparingTo(expenseUpdateVO.amount());
        assertThat(existingExpense.getExpenseDate()).isEqualTo(originalDate);
        assertThat(existingExpense.getDescription()).isEqualTo(expenseUpdateVO.description());
        assertThat(existingExpense.getLocation()).isEqualTo(originalLocation);
    }






}