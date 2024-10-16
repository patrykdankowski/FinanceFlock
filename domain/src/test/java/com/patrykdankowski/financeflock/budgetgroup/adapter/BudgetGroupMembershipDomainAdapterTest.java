package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.budgetgroup.exception.BudgetGroupValidationException;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupValidatorPort;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BudgetGroupMembershipDomainAdapterTest {


    @Mock
    private BudgetGroupValidatorPort budgetGroupValidatorPort;

    @InjectMocks
    private BudgetGroupMembershipDomainAdapter budgetGroupMembershipDomainAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addUserToGroup_shouldAddUserToGroup_whenUserIsNotMemberOfAnyGroup() {
        // given
        UserDomainEntity user = UserDomainEntity.buildUser(1L, "User1", "password", "user1@example.com", LocalDateTime.now());
        BudgetGroupDomainEntity budgetGroup = BudgetGroupDomainEntity.buildBudgetGroup(100L, "Test Group", 1L);

        when(budgetGroupValidatorPort.isNotMemberOfAnyGroup(user)).thenReturn(true);
        doNothing().when(budgetGroupValidatorPort).validateSizeOfGroup(budgetGroup);

        // when
        budgetGroupMembershipDomainAdapter.addUserToGroup(user, budgetGroup);

        // then
        verify(budgetGroupValidatorPort, times(1)).isNotMemberOfAnyGroup(user);
        verify(budgetGroupValidatorPort, times(1)).validateSizeOfGroup(budgetGroup);
        assertThat(user.getBudgetGroupId()).isEqualTo(budgetGroup.getId());
        assertThat(user.getRole()).isEqualTo(Role.GROUP_MEMBER);
        assertThat(budgetGroup.getListOfMembersId()).contains(user.getId());
    }

    @Test
    void addUserToGroup_shouldThrowException_whenUserIsAlreadyMemberOfAnotherGroup() {
        // given
        UserDomainEntity user = UserDomainEntity.buildUser(1L, "User1", "password", "user1@example.com", LocalDateTime.now());
        BudgetGroupDomainEntity budgetGroup = BudgetGroupDomainEntity.buildBudgetGroup(100L, "Test Group", 1L);

        when(budgetGroupValidatorPort.isNotMemberOfAnyGroup(user)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> budgetGroupMembershipDomainAdapter.addUserToGroup(user, budgetGroup))
                .isInstanceOf(BudgetGroupValidationException.class)
                .hasMessageContaining("Cannot add user to budget group because user is already member of some group");

        verify(budgetGroupValidatorPort, times(1)).isNotMemberOfAnyGroup(user);
        verify(budgetGroupValidatorPort, never()).validateSizeOfGroup(budgetGroup);
    }

    @Test
    void removeUserFromGroup_shouldRemoveUserFromGroup_whenUserIsMemberOfGroup() {
        // given
        UserDomainEntity user = UserDomainEntity.buildUser(1L, "User1", "password", "user1@example.com", LocalDateTime.now());
        BudgetGroupDomainEntity budgetGroup = BudgetGroupDomainEntity.buildBudgetGroup(100L, "Test Group", 1L);

        user.manageGroupMembership(budgetGroup.getId(), Role.GROUP_MEMBER);
        budgetGroup.addUser(user.getId());

        when(budgetGroupValidatorPort.isMemberOfGivenIdGroup(user, budgetGroup, budgetGroup.getId())).thenReturn(true);

        // when
        budgetGroupMembershipDomainAdapter.removeUserFromGroup(user, budgetGroup, budgetGroup.getId());

        // then
        verify(budgetGroupValidatorPort, times(1)).isMemberOfGivenIdGroup(user, budgetGroup, budgetGroup.getId());
        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.getBudgetGroupId()).isNull();
        assertThat(budgetGroup.getListOfMembersId()).doesNotContain(user.getId());
    }

    @Test
    void removeUserFromGroup_shouldThrowException_whenUserIsNotMemberOfGroup() {
        // given
        UserDomainEntity user = UserDomainEntity.buildUser(1L, "User1", "password", "user1@example.com", LocalDateTime.now());
        BudgetGroupDomainEntity budgetGroup = BudgetGroupDomainEntity.buildBudgetGroup(100L, "Test Group", 1L);

        when(budgetGroupValidatorPort.isMemberOfGivenIdGroup(user, budgetGroup, budgetGroup.getId())).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> budgetGroupMembershipDomainAdapter.removeUserFromGroup(user, budgetGroup, budgetGroup.getId()))
                .isInstanceOf(BudgetGroupValidationException.class)
                .hasMessageContaining("Cannot remove user from group because this user is not a member of your group");

        verify(budgetGroupValidatorPort, times(1)).isMemberOfGivenIdGroup(user, budgetGroup, budgetGroup.getId());
    }
}