package com.patrykdankowski.financeflock.budgetgroup.adapter;


import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.model.record.BudgetGroupDescription;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupFactoryPort;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BudgetGroupManagementDomainAdapterTest {

    @Mock
    private BudgetGroupFactoryPort budgetGroupFactoryPort;

    @InjectMocks
    private BudgetGroupManagementDomainAdapter budgetGroupManagementDomainAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBudgetGroup_shouldCallFactoryToCreateBudgetGroup() {
        // given
        BudgetGroupDescription budgetGroupDescription = new BudgetGroupDescription("Test Group");
        Long userId = 1L;

        BudgetGroupDomainEntity budgetGroupDomainEntity = BudgetGroupDomainEntity.buildBudgetGroup(
                null, "Test Group", userId);

        when(budgetGroupFactoryPort.createBudgetGroupFromRequest(any(), any())).thenReturn(budgetGroupDomainEntity);

        // when
        BudgetGroupDomainEntity result = budgetGroupManagementDomainAdapter.createBudgetGroup(budgetGroupDescription, userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getOwnerId()).isEqualTo(userId);
        assertThat(result.getDescription()).isEqualTo("Test Group");

        verify(budgetGroupFactoryPort, times(1)).createBudgetGroupFromRequest(userId, budgetGroupDescription);
    }

    @Test
    void createBudgetGroup_shouldThrowExceptionWhenFactoryFails() {
        // given
        BudgetGroupDescription budgetGroupDescription = new BudgetGroupDescription("Test Group");
        Long userId = 1L;

        when(budgetGroupFactoryPort.createBudgetGroupFromRequest(any(), any())).thenThrow(new RuntimeException("Factory error"));

        // when & then
        assertThatThrownBy(() -> budgetGroupManagementDomainAdapter.createBudgetGroup(budgetGroupDescription, userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Factory error");

        verify(budgetGroupFactoryPort, times(1)).createBudgetGroupFromRequest(userId, budgetGroupDescription);
    }

    @Test
    void closeBudgetGroup_shouldResetUsersRolesAndDetachFromGroup() {
        // given
        UserDomainEntity user1 = UserDomainEntity.buildUser(1L, "user1", "password", "user1@example.com", LocalDateTime.now());
        user1.manageGroupMembership(100L, Role.GROUP_MEMBER);

        UserDomainEntity user2 = UserDomainEntity.buildUser(2L, "user2", "password", "user2@example.com", LocalDateTime.now());
        user2.manageGroupMembership(100L, Role.GROUP_ADMIN);

        List<UserDomainEntity> users = List.of(user1, user2);

        // when
        List<UserDomainEntity> result = budgetGroupManagementDomainAdapter.closeBudgetGroup(users);

        // then
        assertThat(result).hasSize(2);

        for (UserDomainEntity user : result) {
            assertThat(user.getRole()).isEqualTo(Role.USER);
            assertThat(user.getBudgetGroupId()).isNull();
        }
    }

    @Test
    void closeBudgetGroup_shouldHandleEmptyUserList() {
        // given
        List<UserDomainEntity> users = List.of();

        // when
        List<UserDomainEntity> result = budgetGroupManagementDomainAdapter.closeBudgetGroup(users);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void closeBudgetGroup_shouldHandleNullUserList() {
        // when & then
        assertThatThrownBy(() -> budgetGroupManagementDomainAdapter.closeBudgetGroup(null))
                .isInstanceOf(NullPointerException.class);
    }
}