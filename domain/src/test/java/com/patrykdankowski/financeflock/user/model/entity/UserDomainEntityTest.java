package com.patrykdankowski.financeflock.user.model.entity;

import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.exception.ToEarlyShareDataPreferenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserDomainEntityTest {
    private UserDomainEntity user;

    @BeforeEach
    void setUp() {
        user = UserDomainEntity.buildUser(1L, "Test User", "password123", "test@example.com", LocalDateTime.now());
    }

    @Test
    void whenBuildUser_thenAllFieldsAreInitializedCorrectly() {
        // Given

        // When
        UserDomainEntity user = UserDomainEntity.buildUser(1L, "Test User", "password123", "test@example.com", LocalDateTime.now());

        // Then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("Test User");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getRole()).isNull();
        assertThat(user.getBudgetGroupId()).isNull();
        assertThat(user.getExpenseListId()).isEmpty();
        assertThat(user.isShareData()).isFalse();
    }

    @Test
    void whenUpdateInfo_thenFieldsAreUpdated() {
        // Given
        LocalDateTime lastToggled = LocalDateTime.now();
        LocalDateTime lastLogged = LocalDateTime.now().minusDays(1);

        // When
        user.updateInfo(true, lastToggled, lastLogged);

        // Then
        assertThat(user.isShareData()).isTrue();
        assertThat(user.getLastToggledShareData()).isEqualTo(lastToggled);
        assertThat(user.getLastLoggedInAt()).isEqualTo(lastLogged);
    }

    @Test
    void givenUserWithNoRole_whenChangeRole_thenRoleIsUpdated() {
        // Given
        Role newRole = Role.GROUP_ADMIN;

        // When
        user.changeRole(newRole);

        // Then
        assertThat(user.getRole()).isEqualTo(newRole);
    }

    @Test
    void givenUserWithSameRole_whenChangeRole_thenRoleRemainsUnchanged() {
        // Given
        user.changeRole(Role.GROUP_ADMIN);

        // When
        user.changeRole(Role.GROUP_ADMIN);

        // Then
        assertThat(user.getRole()).isEqualTo(Role.GROUP_ADMIN);
    }

    @Test
    void givenUserWithDifferentRole_whenChangeRole_thenRoleIsUpdated() {
        // Given
        user.changeRole(Role.USER);

        // When
        user.changeRole(Role.GROUP_ADMIN);

        // Then
        assertThat(user.getRole()).isEqualTo(Role.GROUP_ADMIN);
    }

    @Test
    void whenManageGroupMembership_thenGroupAndRoleAreUpdated() {
        // Given
        Long groupId = 2L;
        Role role = Role.GROUP_ADMIN;

        // When
        user.manageGroupMembership(groupId, role);

        // Then
        assertThat(user.getBudgetGroupId()).isEqualTo(groupId);
        assertThat(user.getRole()).isEqualTo(role);
    }

    @Test
    void whenLeaveGroup_thenGroupIdIsNullAndRoleIsUser() {
        // Given

        // When
        user.manageGroupMembership(null, Role.USER);

        // Then
        assertThat(user.getBudgetGroupId()).isNull();
        assertThat(user.getRole()).isEqualTo(Role.USER);
    }

    @Test
    void givenNegativeGroupId_whenManageGroupMembership_thenThrowIllegalStateException() {
        // Given
        Long groupId = -1L;

        // When & Then
        assertThatThrownBy(() -> user.manageGroupMembership(groupId, Role.USER))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Given id group is less than 0");
    }

    @Test
    void whenAddExpense_thenExpenseIsAddedToList() {
        // Given
        Long expenseId = 10L;

        // When
        user.addExpense(expenseId);

        // Then
        assertThat(user.getExpenseListId()).contains(expenseId);
    }

    @Test
    void givenExistingExpense_whenAddExpense_thenExpenseIsNotDuplicated() {
        // Given
        Long expenseId = 10L;
        user.addExpense(expenseId);

        // When
        user.addExpense(expenseId);

        // Then
        assertThat(user.getExpenseListId()).hasSize(1);
    }

    @Test
    void whenRemoveExpense_thenExpenseIsRemovedFromList() {
        // Given
        Long expenseId = 10L;
        user.addExpense(expenseId);

        // When
        user.removeExpense(expenseId);

        // Then
        assertThat(user.getExpenseListId()).doesNotContain(expenseId);
    }

    @Test
    void givenNullExpenseId_whenRemoveExpense_thenNoActionTaken() {
        // Given
        Long expenseId = 10L;
        user.addExpense(expenseId);

        // When
        user.removeExpense(null);

        // Then
        assertThat(user.getExpenseListId()).contains(expenseId);
    }

    @Test
    void givenToggledShareData_whenToggleShareDataTooEarly_thenThrowException() {
        // Given
        user.toggleShareData();

        // When & Then
        assertThatThrownBy(user::toggleShareData)
                .isInstanceOf(ToEarlyShareDataPreferenceException.class);
    }

    @Test
    void givenToggledShareData_whenToggleShareDataAfterCooldown_thenDataIsToggled() {
        // Given
        user.toggleShareData();
        LocalDateTime newTime = user.getLastToggledShareData().minusMinutes(10);
        user.updateInfo(user.isShareData(), newTime, user.getLastLoggedInAt());

        // When
        user.toggleShareData();

        // Then
        assertThat(user.isShareData()).isFalse();
    }

    @Test
    void whenInitializeShareDataForNewUser_thenShareDataIsInitialized() {
        // Given
        UserDomainEntity newUser = new UserDomainEntity();

        // When
        newUser.initializeShareData();

        // Then
        assertThat(newUser.isShareData()).isTrue();
    }

    @Test
    void givenExistingUser_whenInitializeShareData_thenThrowIllegalStateException() {
        // Given

        // When & Then
        assertThatThrownBy(() -> user.initializeShareData())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Cannot initialize shareData for an existing user.");
    }
    @Test
    void whenBuildUserWithNullValues_thenFieldsAreNull() {
        // Given
        String name = null;
        String password = null;
        String email = null;
        LocalDateTime createdAt = null;

        // When
        UserDomainEntity user = UserDomainEntity.buildUser(2L, name, password, email, createdAt);

        // Then
        assertThat(user.getName()).isNull();
        assertThat(user.getPassword()).isNull();
        assertThat(user.getEmail()).isNull();
        assertThat(user.getCreatedAt()).isNull();
    }

    @Test
    void givenUserWithExistingGroup_whenManageGroupMembership_thenGroupAndRoleAreUpdated() {
        // Given
        Long initialGroupId = 2L;
        user.manageGroupMembership(initialGroupId, Role.GROUP_ADMIN);

        Long newGroupId = 3L;
        Role newRole = Role.GROUP_MEMBER;

        // When
        user.manageGroupMembership(newGroupId, newRole);

        // Then
        assertThat(user.getBudgetGroupId()).isEqualTo(newGroupId);
        assertThat(user.getRole()).isEqualTo(newRole);
    }

    @Test
    void whenUpdateLastLoggedInAtWithNull_thenFieldIsSetToNull() {
        // Given
        LocalDateTime lastLoggedIn = null;

        // When
        user.updateInfo(user.isShareData(), user.getLastToggledShareData(), lastLoggedIn);

        // Then
        assertThat(user.getLastLoggedInAt()).isNull();
    }

    @Test
    void givenShareDataToggled_whenToggleImmediately_thenThrowException() {
        // Given
        user.toggleShareData();
        LocalDateTime immediateTime = user.getLastToggledShareData().plusSeconds(1);
        user.updateInfo(user.isShareData(), immediateTime, user.getLastLoggedInAt());

        // When & Then
        assertThatThrownBy(user::toggleShareData)
                .isInstanceOf(ToEarlyShareDataPreferenceException.class);
    }

    @Test
    void givenShareDataToggled_whenToggleAfterFiveMinutes_thenTogglesSuccessfully() {
        // Given
        user.toggleShareData();
        LocalDateTime fiveMinutesLater = user.getLastToggledShareData().minusMinutes(5);
        user.updateInfo(user.isShareData(), fiveMinutesLater, user.getLastLoggedInAt());

        // When
        user.toggleShareData();

        // Then
        assertThat(user.isShareData()).isFalse();
    }
}
