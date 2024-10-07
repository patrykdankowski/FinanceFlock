package com.patrykdankowski.financeflock.user.adapter;

import com.patrykdankowski.financeflock.auth.port.AuthenticationServicePort;
import com.patrykdankowski.financeflock.budgetgroup.exception.BudgetGroupValidationException;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupCommandServicePort;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.exception.AdminToggleShareDataException;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.port.UserCommandServicePort;
import com.patrykdankowski.financeflock.user.port.UserMembershipDomainPort;
import com.patrykdankowski.financeflock.user.port.UserValidatorPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserFacadeAdapterTest {

    @Mock
    private BudgetGroupCommandServicePort budgetGroupCommandService;

    @Mock
    private UserMembershipDomainPort userMembershipDomain;

    @Mock
    private UserCommandServicePort userCommandService;

    @Mock
    private AuthenticationServicePort authenticationService;

    @Mock
    private UserValidatorPort userValidator;

    @InjectMocks
    private UserFacadeAdapter userFacadeAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void leaveBudgetGroup_shouldLeaveSuccessfully_whenUserIsGroupMember() {
        Long groupId = 1L;
        UserDomainEntity loggedUser = mock(UserDomainEntity.class);
        BudgetGroupDomainEntity budgetGroup = mock(BudgetGroupDomainEntity.class);

        when(authenticationService.getFullUserFromContext()).thenReturn(loggedUser);
        when(budgetGroupCommandService.findBudgetGroupById(anyLong())).thenReturn(budgetGroup);
        when(userValidator.hasGivenRole(loggedUser, Role.GROUP_MEMBER)).thenReturn(true);

        userFacadeAdapter.leaveBudgetGroup(groupId);

        verify(userMembershipDomain, times(1)).leaveBudgetGroup(loggedUser, budgetGroup, groupId);
        verify(userCommandService, times(1)).saveUser(loggedUser);
        verify(budgetGroupCommandService, times(1)).saveBudgetGroup(budgetGroup);
    }

    @Test
    void toggleShareData_shouldToggleSuccessfully_whenUserIsNotAdmin() {
        UserDomainEntity loggedUser = mock(UserDomainEntity.class);
        when(authenticationService.getFullUserFromContext()).thenReturn(loggedUser);
        when(userValidator.hasGivenRole(loggedUser, Role.GROUP_ADMIN)).thenReturn(false);
        when(userMembershipDomain.toggleShareData(loggedUser)).thenReturn(true);

        boolean result = userFacadeAdapter.toggleShareData();

        assertThat(result).isTrue();
        verify(userCommandService, times(1)).saveUser(loggedUser);
        verify(userMembershipDomain, times(1)).toggleShareData(loggedUser);
    }


    @Test
    void leaveBudgetGroup_shouldThrowException_whenUserIsGroupAdmin() {
        Long groupId = 1L;
        UserDomainEntity loggedUser = mock(UserDomainEntity.class);
        BudgetGroupDomainEntity budgetGroup = mock(BudgetGroupDomainEntity.class);

        when(authenticationService.getFullUserFromContext()).thenReturn(loggedUser);
        when(budgetGroupCommandService.findBudgetGroupById(anyLong())).thenReturn(budgetGroup);
        when(userValidator.hasGivenRole(loggedUser, Role.GROUP_MEMBER)).thenReturn(false);

        assertThatThrownBy(() -> userFacadeAdapter.leaveBudgetGroup(groupId))
                .isInstanceOf(BudgetGroupValidationException.class)
                .hasMessageContaining("Cannot leave budget group as admin");

        verify(userMembershipDomain, never()).leaveBudgetGroup(any(), any(), any());
        verify(userCommandService, never()).saveUser(any());
        verify(budgetGroupCommandService, never()).saveBudgetGroup(any());
    }

    @Test
    void toggleShareData_shouldThrowException_whenUserIsAdminAndGroupIsNotNull() {
        UserDomainEntity loggedUser = mock(UserDomainEntity.class);
        when(authenticationService.getFullUserFromContext()).thenReturn(loggedUser);
        when(userValidator.hasGivenRole(loggedUser, Role.GROUP_ADMIN)).thenReturn(true);
        when(userValidator.groupIsNull(loggedUser)).thenReturn(false);

        assertThatThrownBy(() -> userFacadeAdapter.toggleShareData())
                .isInstanceOf(AdminToggleShareDataException.class);

        verify(userMembershipDomain, never()).toggleShareData(any());
        verify(userCommandService, never()).saveUser(any());
    }

    @Test
    void toggleShareData_shouldThrowException_whenUserNotAuthenticated() {
        when(authenticationService.getFullUserFromContext()).thenThrow(new SecurityException("User not authenticated"));

        assertThatThrownBy(() -> userFacadeAdapter.toggleShareData())
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("User not authenticated");

        verify(userMembershipDomain, never()).toggleShareData(any());
        verify(userCommandService, never()).saveUser(any());
    }

    @Test
    void leaveBudgetGroup_shouldThrowException_whenBudgetGroupNotFound() {
        Long groupId = 1L;
        UserDomainEntity loggedUser = mock(UserDomainEntity.class);

        when(authenticationService.getFullUserFromContext()).thenReturn(loggedUser);
        when(budgetGroupCommandService.findBudgetGroupById(anyLong())).thenThrow(new RuntimeException("Budget group not found"));

        assertThatThrownBy(() -> userFacadeAdapter.leaveBudgetGroup(groupId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Budget group not found");

        verify(userMembershipDomain, never()).leaveBudgetGroup(any(), any(), any());
        verify(userCommandService, never()).saveUser(any());
        verify(budgetGroupCommandService, never()).saveBudgetGroup(any());
    }

    @Test
    void toggleShareData_shouldThrowException_whenToggleTooFrequently() {
        UserDomainEntity loggedUser = mock(UserDomainEntity.class);
        when(authenticationService.getFullUserFromContext()).thenReturn(loggedUser);
        doThrow(new IllegalStateException("Cannot toggle sharing data too frequently"))
                .when(userMembershipDomain).toggleShareData(loggedUser);

        assertThatThrownBy(() -> userFacadeAdapter.toggleShareData())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot toggle sharing data too frequently");

        verify(userMembershipDomain, times(1)).toggleShareData(loggedUser);
        verify(userCommandService, never()).saveUser(loggedUser);
    }

}
