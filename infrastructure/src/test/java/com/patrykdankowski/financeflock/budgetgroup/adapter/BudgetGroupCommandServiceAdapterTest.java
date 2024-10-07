package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.budgetgroup.exception.BudgetGroupNotFoundException;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupCommandRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class BudgetGroupCommandServiceAdapterTest {

    @Mock
    private BudgetGroupCommandRepositoryPort budgetGroupCommandRepository;

    @InjectMocks
    private BudgetGroupCommandServiceAdapter budgetGroupCommandServiceAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void saveBudgetGroup_shouldReturnSavedBudgetGroup_whenValidDataProvided() {
        Long groupId = 1L;
        String description = "Test Group";
        Long ownerId = 100L;

        BudgetGroupDomainEntity budgetGroupDomainEntity = BudgetGroupDomainEntity.buildBudgetGroup(groupId, description, ownerId);
        when(budgetGroupCommandRepository.save(budgetGroupDomainEntity)).thenReturn(budgetGroupDomainEntity);

        BudgetGroupDomainEntity result = budgetGroupCommandServiceAdapter.saveBudgetGroup(budgetGroupDomainEntity);

        assertThat(result).isEqualTo(budgetGroupDomainEntity);
        verify(budgetGroupCommandRepository, times(1)).save(budgetGroupDomainEntity);
    }


    @Test
    void deleteBudgetGroup_shouldCallRepositoryDeleteMethod_whenBudgetGroupExists() {
        Long groupId = 1L;
        String description = "Test Group";
        Long ownerId = 100L;

        BudgetGroupDomainEntity budgetGroupDomainEntity = BudgetGroupDomainEntity.buildBudgetGroup(groupId, description, ownerId);

        budgetGroupCommandServiceAdapter.deleteBudgetGroup(budgetGroupDomainEntity);

        verify(budgetGroupCommandRepository, times(1)).delete(budgetGroupDomainEntity);
    }


    @Test
    void findBudgetGroupById_shouldReturnBudgetGroup_whenGroupExists() {
        Long groupId = 1L;
        String description = "Test Group";
        Long ownerId = 100L;

        BudgetGroupDomainEntity budgetGroupDomainEntity = BudgetGroupDomainEntity.buildBudgetGroup(groupId, description, ownerId);
        when(budgetGroupCommandRepository.findById(groupId)).thenReturn(Optional.of(budgetGroupDomainEntity));

        BudgetGroupDomainEntity result = budgetGroupCommandServiceAdapter.findBudgetGroupById(groupId);

        assertThat(result).isEqualTo(budgetGroupDomainEntity);
        verify(budgetGroupCommandRepository, times(1)).findById(groupId);
    }

    @Test
    void findBudgetGroupById_shouldThrowException_whenGroupDoesNotExist() {
        Long groupId = 99L;
        when(budgetGroupCommandRepository.findById(groupId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> budgetGroupCommandServiceAdapter.findBudgetGroupById(groupId))
                .isInstanceOf(BudgetGroupNotFoundException.class)
                .hasMessageContaining("Budget group with ID " + groupId + " not found");

        verify(budgetGroupCommandRepository, times(1)).findById(groupId);
    }
}

