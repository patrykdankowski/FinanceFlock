package com.patrykdankowski.financeflock.user.adapter;


import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupValidatorPort;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.exception.ToEarlyShareDataPreferenceException;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class UserMembershipDomainAdapterTest {

    @Mock
    private BudgetGroupValidatorPort budgetGroupValidator;

    @InjectMocks
    private UserMembershipDomainAdapter userMembershipDomainAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void leaveBudgetGroup_shouldCallValidatorAndRemoveUserFromGroup() {
        // Arrange
        UserDomainEntity user = UserDomainEntity.buildUser(1L, "User1", "password", "user1@example.com", LocalDateTime.now());
        user.manageGroupMembership(100L, Role.GROUP_MEMBER);
        BudgetGroupDomainEntity budgetGroup = BudgetGroupDomainEntity.buildBudgetGroup(100L, "Test Group", user.getId());

        // Act
        userMembershipDomainAdapter.leaveBudgetGroup(user, budgetGroup, 100L);

        // Assert
        verify(budgetGroupValidator, times(1)).validateMembership(user, budgetGroup, 100L);
        assertThat(user.getBudgetGroupId()).isNull();
        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(budgetGroup.getListOfMembersId()).doesNotContain(user.getId());
    }

    @Test
    void leaveBudgetGroup_shouldThrowException_whenValidationFails() {
        // Arrange
        UserDomainEntity user = UserDomainEntity.buildUser(1L, "User1", "password", "user1@example.com", LocalDateTime.now());
        BudgetGroupDomainEntity budgetGroup = BudgetGroupDomainEntity.buildBudgetGroup(100L, "Test Group", user.getId());

        // Mock failure in validation
        doThrow(new IllegalArgumentException("User is not a member of the group"))
                .when(budgetGroupValidator).validateMembership(user, budgetGroup, 100L);

        // Act & Assert
        assertThatThrownBy(() -> userMembershipDomainAdapter.leaveBudgetGroup(user, budgetGroup, 100L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User is not a member of the group");
    }

    @Test
    void toggleShareData_shouldToggleSharingPreference() {
        UserDomainEntity user = UserDomainEntity.buildUser(1L, "User1", "password", "user1@example.com", LocalDateTime.now());

        assertThat(user.isShareData()).isFalse();

        boolean resultAfterFirstToggle = userMembershipDomainAdapter.toggleShareData(user);


        assertThat(resultAfterFirstToggle).isTrue();
        assertThat(user.isShareData()).isTrue();
    }

    @Test
    void toggleShareData_shouldThrowException_whenToggleIsTooFrequent() {
        UserDomainEntity user = UserDomainEntity.buildUser(1L, "User1", "password", "user1@example.com", LocalDateTime.now());

        userMembershipDomainAdapter.toggleShareData(user);

        LocalDateTime lastToggledTime = user.getLastToggledShareData();

        assertThatThrownBy(() -> {
            userMembershipDomainAdapter.toggleShareData(user);
        })
                .isInstanceOf(ToEarlyShareDataPreferenceException.class)
                .hasMessageContaining("Cannot toggle sharing preference before 5 minutes have passed since: " + lastToggledTime.toString());
    }


    @Test
    void leaveBudgetGroup_shouldNotThrow_whenUserLeavesSuccessfully() {
        // given
        UserDomainEntity user = UserDomainEntity.buildUser(1L, "User1", "password", "user1@example.com", LocalDateTime.now());
        user.manageGroupMembership(100L, Role.GROUP_MEMBER);
        BudgetGroupDomainEntity budgetGroup = BudgetGroupDomainEntity.buildBudgetGroup(100L, "Test Group", user.getId());
        budgetGroup.addUser(user.getId());

        // when & then
        assertDoesNotThrow(() -> userMembershipDomainAdapter.leaveBudgetGroup(user, budgetGroup, 100L));
        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.getBudgetGroupId()).isNull();
        assertThat(budgetGroup.getListOfMembersId()).doesNotContain(user.getId());
    }


    @Test
    void leaveBudgetGroup_shouldRemoveUser_whenUserIsLastInGroup() {
        // given
        UserDomainEntity user = UserDomainEntity.buildUser(1L, "User1", "password", "user1@example.com", LocalDateTime.now());
        user.manageGroupMembership(100L, Role.GROUP_ADMIN);
        BudgetGroupDomainEntity budgetGroup = BudgetGroupDomainEntity.buildBudgetGroup(100L, "Test Group", user.getId());
        budgetGroup.addUser(user.getId());

        // when
        userMembershipDomainAdapter.leaveBudgetGroup(user, budgetGroup, 100L);

        // then
        assertThat(budgetGroup.getListOfMembersId()).isEmpty();
        assertThat(user.getBudgetGroupId()).isNull();
        assertThat(user.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void leaveBudgetGroup_shouldRemoveUser_whenUserIsNotAdmin() {
        // given
        UserDomainEntity user = UserDomainEntity.buildUser(2L, "User2", "password", "user2@example.com", LocalDateTime.now());
        user.manageGroupMembership(100L, Role.GROUP_MEMBER);
        BudgetGroupDomainEntity budgetGroup = BudgetGroupDomainEntity.buildBudgetGroup(100L, "Test Group", 1L);
        budgetGroup.addUser(1L);
        budgetGroup.addUser(user.getId());

        // when
        userMembershipDomainAdapter.leaveBudgetGroup(user, budgetGroup, 100L);

        // then
        assertThat(budgetGroup.getListOfMembersId()).doesNotContain(user.getId());
        assertThat(user.getBudgetGroupId()).isNull();
        assertThat(user.getRole()).isEqualTo(Role.USER);
    }
}
