package com.patrykdankowski.financeflock.user.adapter;

import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.exception.BadRoleException;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserValidatorAdapterTest {

    private UserValidatorAdapter userValidatorAdapter;

    @BeforeEach
    void setUp() {
        userValidatorAdapter = new UserValidatorAdapter();
    }

    // Testy dla metody hasGivenRole

    @Test
    void hasGivenRole_shouldReturnTrue_whenUserHasGivenRole() {
        // Arrange
        UserDomainEntity user = UserDomainEntity.buildUser(1L, "John Doe", "password", "john@example.com", null);
        user.changeRole(Role.GROUP_ADMIN);

        // Act
        boolean result = userValidatorAdapter.hasGivenRole(user, Role.GROUP_ADMIN);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void hasGivenRole_shouldReturnFalse_whenUserDoesNotHaveGivenRole() {
        // Arrange
        UserDomainEntity user = UserDomainEntity.buildUser(1L, "Jane Doe", "password", "jane@example.com", null);
        user.changeRole(Role.USER);

        // Act
        boolean result = userValidatorAdapter.hasGivenRole(user, Role.GROUP_ADMIN);

        // Assert
        assertThat(result).isFalse();
    }

    // Testy dla metody groupIsNull

    @Test
    void groupIsNull_shouldReturnTrue_whenUserHasNoGroup() {
        // Arrange
        UserDomainEntity user = UserDomainEntity.buildUser(1L, "John Doe", "password", "john@example.com", null);
        user.manageGroupMembership(null, Role.USER);

        // Act
        boolean result = userValidatorAdapter.groupIsNull(user);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void groupIsNull_shouldReturnFalse_whenUserHasGroup() {
        // Arrange
        UserDomainEntity user = UserDomainEntity.buildUser(1L, "Jane Doe", "password", "jane@example.com", null);
        user.manageGroupMembership(2L, Role.USER);

        // Act
        boolean result = userValidatorAdapter.groupIsNull(user);

        // Assert
        assertThat(result).isFalse();
    }

    // Testy dla metody validateRole

    @Test
    void validateRole_shouldNotThrowException_whenUserHasGivenRole() {
        // Arrange
        UserDomainEntity user = UserDomainEntity.buildUser(1L, "John Doe", "password", "john@example.com", null);
        user.changeRole(Role.USER);

        // Act & Assert
        userValidatorAdapter.validateRole(user, Role.USER);  // Powinno zakończyć się bez wyjątku
    }

    @Test
    void validateRole_shouldThrowBadRoleException_whenUserDoesNotHaveGivenRole() {
        // Arrange
        UserDomainEntity user = UserDomainEntity.buildUser(1L, "Jane Doe", "password", "jane@example.com", null);
        user.changeRole(Role.GROUP_ADMIN);

        // Act & Assert
        assertThatThrownBy(() -> userValidatorAdapter.validateRole(user, Role.USER))
                .isInstanceOf(BadRoleException.class)
                .hasMessageContaining("Bad role");
    }
}
