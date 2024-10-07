package com.patrykdankowski.financeflock.budgetgroup.adapter;


import com.patrykdankowski.financeflock.auth.port.AuthenticationServicePort;
import com.patrykdankowski.financeflock.budgetgroup.dto.BudgetGroupDto;
import com.patrykdankowski.financeflock.budgetgroup.dto.EmailDto;
import com.patrykdankowski.financeflock.budgetgroup.exception.BudgetGroupValidationException;
import com.patrykdankowski.financeflock.budgetgroup.exception.SelfManagementInGroupException;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupCommandServicePort;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupManagementDomainPort;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupMembershipDomainPort;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.port.UserCommandServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BudgetGroupFacadeAdapterTest {

    @Mock
    private BudgetGroupMembershipDomainPort budgetGroupMembershipDomain;

    @Mock
    private BudgetGroupManagementDomainPort budgetGroupManagementDomain;

    @Mock
    private UserCommandServicePort userCommandService;

    @Mock
    private BudgetGroupCommandServicePort budgetGroupCommandService;

    @Mock
    private AuthenticationServicePort authenticationService;

    @Mock
    private BudgetGroupValidatorAdapter budgetGroupValidator;

    @InjectMocks
    private BudgetGroupFacadeAdapter budgetGroupFacadeAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBudgetGroup_shouldCreateGroup_whenUserIsNotMemberOfAnyGroup() {
        BudgetGroupDto budgetGroupDto = new BudgetGroupDto("Valid Description");

        UserDomainEntity loggedUser = mock(UserDomainEntity.class);
        BudgetGroupDomainEntity budgetGroupDomain = mock(BudgetGroupDomainEntity.class);

        when(authenticationService.getFullUserFromContext()).thenReturn(loggedUser);
        when(budgetGroupValidator.isNotMemberOfAnyGroup(loggedUser)).thenReturn(true);
        when(budgetGroupManagementDomain.createBudgetGroup(any(), anyLong())).thenReturn(budgetGroupDomain);
        when(budgetGroupCommandService.saveBudgetGroup(budgetGroupDomain)).thenReturn(budgetGroupDomain);
        when(budgetGroupDomain.getId()).thenReturn(1L);

        Long result = budgetGroupFacadeAdapter.createBudgetGroup(budgetGroupDto);

        assertThat(result).isEqualTo(1L);
        verify(userCommandService, times(1)).saveUser(loggedUser);
    }

    @Test
    void createBudgetGroup_shouldThrowException_whenUserIsMemberOfGroup() {
        BudgetGroupDto budgetGroupDto = new BudgetGroupDto("Valid Description");
        UserDomainEntity loggedUser = mock(UserDomainEntity.class);

        when(authenticationService.getFullUserFromContext()).thenReturn(loggedUser);
        when(budgetGroupValidator.isNotMemberOfAnyGroup(loggedUser)).thenReturn(false);

        assertThatThrownBy(() -> budgetGroupFacadeAdapter.createBudgetGroup(budgetGroupDto))
                .isInstanceOf(BudgetGroupValidationException.class)
                .hasMessageContaining("Cannot create budget group, probably u are a member of different group.");
    }

    @Test
    void addUserToGroup_shouldThrowException_whenAddingSelfToGroup() {
        Long groupId = 1L;
        EmailDto emailDto = new EmailDto("user@example.com");
        UserDomainEntity loggedUser = mock(UserDomainEntity.class);
        UserDomainEntity userToAdd = mock(UserDomainEntity.class);

        when(authenticationService.getFullUserFromContext()).thenReturn(loggedUser);
        when(userCommandService.findUserByEmail(emailDto.getEmail())).thenReturn(userToAdd);
        when(loggedUser.getId()).thenReturn(1L);
        when(userToAdd.getId()).thenReturn(1L);

        assertThatThrownBy(() -> budgetGroupFacadeAdapter.addUserToGroup(emailDto, groupId))
                .isInstanceOf(SelfManagementInGroupException.class)
                .hasMessageContaining("You cannot perform this action on yourself in the group.");
    }

    @Test
    void closeBudgetGroup_shouldCloseGroup_whenUserIsAdmin() {
        Long groupId = 1L;
        UserDomainEntity loggedUser = mock(UserDomainEntity.class);
        BudgetGroupDomainEntity budgetGroupDomain = mock(BudgetGroupDomainEntity.class);
        List<UserDomainEntity> users = Collections.singletonList(mock(UserDomainEntity.class));

        when(authenticationService.getFullUserFromContext()).thenReturn(loggedUser);
        when(budgetGroupCommandService.findBudgetGroupById(groupId)).thenReturn(budgetGroupDomain);
        doNothing().when(budgetGroupValidator).validateIfUserIsAdmin(loggedUser, groupId, budgetGroupDomain);
        when(userCommandService.listOfUsersFromIds(any())).thenReturn(users);
        when(budgetGroupManagementDomain.closeBudgetGroup(users)).thenReturn(users);

        budgetGroupFacadeAdapter.closeBudgetGroup(groupId);

        verify(budgetGroupCommandService, times(1)).deleteBudgetGroup(budgetGroupDomain);
        verify(userCommandService, times(1)).saveAllUsers(users);
    }

    @Test
    void removeUserFromGroup_shouldThrowException_whenRemovingSelfFromGroup() {
        Long groupId = 1L;
        EmailDto emailDto = new EmailDto("user@example.com");
        UserDomainEntity loggedUser = mock(UserDomainEntity.class);
        UserDomainEntity userToRemove = mock(UserDomainEntity.class);

        when(authenticationService.getFullUserFromContext()).thenReturn(loggedUser);
        when(userCommandService.findUserByEmail(emailDto.getEmail())).thenReturn(userToRemove);
        when(loggedUser.getId()).thenReturn(1L);
        when(userToRemove.getId()).thenReturn(1L);

        assertThatThrownBy(() -> budgetGroupFacadeAdapter.removeUserFromGroup(emailDto, groupId))
                .isInstanceOf(SelfManagementInGroupException.class)
                .hasMessageContaining("You cannot perform this action on yourself in the group.");
    }

    @Test
    void addUserToGroup_shouldThrowException_whenUserTriesToAddThemselves() {
        Long groupId = 1L;
        UserDomainEntity loggedUser = mock(UserDomainEntity.class);
        EmailDto emailDto = new EmailDto("user@example.com");

        when(authenticationService.getFullUserFromContext()).thenReturn(loggedUser);
        when(loggedUser.getId()).thenReturn(1L);
        when(userCommandService.findUserByEmail(emailDto.getEmail())).thenReturn(loggedUser);

        assertThatThrownBy(() -> budgetGroupFacadeAdapter.addUserToGroup(emailDto, groupId))
                .isInstanceOf(SelfManagementInGroupException.class)
                .hasMessage("You cannot perform this action on yourself in the group.");
    }

    @Test
    void createBudgetGroup_shouldThrowException_whenUserIsAlreadyMemberOfAnotherGroup() {
        BudgetGroupDto budgetGroupDto = new BudgetGroupDto("Valid Description");
        UserDomainEntity loggedUser = mock(UserDomainEntity.class);

        when(authenticationService.getFullUserFromContext()).thenReturn(loggedUser);
        when(budgetGroupValidator.isNotMemberOfAnyGroup(loggedUser)).thenReturn(false);

        assertThatThrownBy(() -> budgetGroupFacadeAdapter.createBudgetGroup(budgetGroupDto))
                .isInstanceOf(BudgetGroupValidationException.class)
                .hasMessageContaining("Cannot create budget group, probably u are a member of different group.");
    }

    @Test
    void closeBudgetGroup_shouldThrowException_whenUserIsNotAdmin() {
        Long groupId = 1L;
        UserDomainEntity loggedUser = mock(UserDomainEntity.class);
        BudgetGroupDomainEntity budgetGroupDomain = mock(BudgetGroupDomainEntity.class);

        when(authenticationService.getFullUserFromContext()).thenReturn(loggedUser);
        when(budgetGroupCommandService.findBudgetGroupById(groupId)).thenReturn(budgetGroupDomain);

        doThrow(new BudgetGroupValidationException("User is not an admin of this group"))
                .when(budgetGroupValidator).validateIfUserIsAdmin(loggedUser, groupId, budgetGroupDomain);

        assertThatThrownBy(() -> budgetGroupFacadeAdapter.closeBudgetGroup(groupId))
                .isInstanceOf(BudgetGroupValidationException.class)
                .hasMessageContaining("User is not an admin of this group");
    }


    @Test
    void closeBudgetGroup_shouldNotDeleteGroup_whenValidationFails() {
        Long groupId = 1L;
        UserDomainEntity loggedUser = mock(UserDomainEntity.class);
        BudgetGroupDomainEntity budgetGroupDomain = mock(BudgetGroupDomainEntity.class);

        when(authenticationService.getFullUserFromContext()).thenReturn(loggedUser);
        when(budgetGroupCommandService.findBudgetGroupById(groupId)).thenReturn(budgetGroupDomain);
        doThrow(new BudgetGroupValidationException("User is not an admin"))
                .when(budgetGroupValidator).validateIfUserIsAdmin(loggedUser, groupId, budgetGroupDomain);

        assertThatThrownBy(() -> budgetGroupFacadeAdapter.closeBudgetGroup(groupId))
                .isInstanceOf(BudgetGroupValidationException.class)
                .hasMessageContaining("User is not an admin");

        verify(budgetGroupCommandService, never()).deleteBudgetGroup(budgetGroupDomain);
        verify(userCommandService, never()).saveAllUsers(any());
    }
}