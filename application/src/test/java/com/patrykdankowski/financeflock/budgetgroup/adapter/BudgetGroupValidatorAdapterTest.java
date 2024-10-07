package com.patrykdankowski.financeflock.budgetgroup.adapter;


import com.patrykdankowski.financeflock.budgetgroup.exception.BudgetGroupValidationException;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.common.AppConstants;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.port.UserValidatorPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BudgetGroupValidatorAdapterTest {

    @Mock
    private UserValidatorPort userValidator;

    @InjectMocks
    private BudgetGroupValidatorAdapter budgetGroupValidatorAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateMembership_shouldThrowException_whenUserIsNotAMember() {
        // given
        UserDomainEntity user = UserDomainEntity.buildUser(1L, "User1", "password", "user1@example.com", LocalDateTime.now());
        BudgetGroupDomainEntity budgetGroup = BudgetGroupDomainEntity.buildBudgetGroup(100L, "Test Group", 1L);
        user.manageGroupMembership(null, Role.USER);
        // when & then
        assertThatThrownBy(() -> budgetGroupValidatorAdapter.validateMembership(user, budgetGroup, 100L))
                .isInstanceOf(BudgetGroupValidationException.class)
                .hasMessageContaining("U are not a member of given id group");
    }


    @Test
    void validateSizeOfGroup_shouldPass_whenGroupIsNotFull() {
        // given
        BudgetGroupDomainEntity budgetGroup = BudgetGroupDomainEntity.buildBudgetGroup(100L, "Test Group", 1L);
        Set<Long> members = new HashSet<>();
        for (int i = 0; i < AppConstants.MAX_BUDGET_GROUP_SIZE - 1; i++) {
            members.add((long) i);
        }
        budgetGroup.updateListOfMembers(members);

        // when
        budgetGroupValidatorAdapter.validateSizeOfGroup(budgetGroup);

    }

    @Test
    void isNotMemberOfAnyGroup_shouldReturnTrue_whenUserIsNotInAnyGroup() {
        // given
        UserDomainEntity user = UserDomainEntity.buildUser(1L, "User1", "password", "user1@example.com", LocalDateTime.now());
        user.manageGroupMembership(null, Role.USER);

        when(userValidator.hasGivenRole(user, Role.USER)).thenReturn(true);
        when(userValidator.groupIsNull(user)).thenReturn(true);

        // when
        boolean result = budgetGroupValidatorAdapter.isNotMemberOfAnyGroup(user);

        // then
        verify(userValidator, times(1)).hasGivenRole(user, Role.USER);
        verify(userValidator, times(1)).groupIsNull(user);
        assertThat(result).isTrue();
    }

    @Test
    void isNotMemberOfAnyGroup_shouldReturnFalse_whenUserIsInGroup() {
        // given
        UserDomainEntity user = UserDomainEntity.buildUser(1L, "User1", "password", "user1@example.com", LocalDateTime.now());
        user.manageGroupMembership(100L, Role.GROUP_MEMBER);

        when(userValidator.hasGivenRole(user, Role.USER)).thenReturn(false);
        when(userValidator.groupIsNull(user)).thenReturn(false);

        // when
        boolean result = budgetGroupValidatorAdapter.isNotMemberOfAnyGroup(user);

        // then
        verify(userValidator, times(1)).hasGivenRole(user, Role.USER);
        verify(userValidator, times(1)).groupIsNull(user);
        assertThat(result).isFalse();
    }

    @Test
    void isMemberOfGivenIdGroup_shouldReturnTrue_whenUserIsValidMember() {
        // given
        UserDomainEntity user = UserDomainEntity.buildUser(1L, "User1", "password", "user1@example.com", LocalDateTime.now());
        BudgetGroupDomainEntity budgetGroup = BudgetGroupDomainEntity.buildBudgetGroup(100L, "Test Group", user.getId());
        user.manageGroupMembership(100L, Role.GROUP_MEMBER);
        budgetGroup.addUser(user.getId());

        when(userValidator.hasGivenRole(user, Role.GROUP_MEMBER)).thenReturn(true);
        when(userValidator.groupIsNull(user)).thenReturn(false);

        // when
        boolean result = budgetGroupValidatorAdapter.isMemberOfGivenIdGroup(user, budgetGroup, 100L);

        // then
        assertThat(result).isTrue();
        verify(userValidator, times(1)).hasGivenRole(user, Role.GROUP_MEMBER);
        verify(userValidator, times(1)).groupIsNull(user);
    }
}