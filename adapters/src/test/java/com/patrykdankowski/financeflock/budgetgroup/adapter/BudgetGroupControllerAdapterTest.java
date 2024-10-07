package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.budgetgroup.dto.BudgetGroupDto;
import com.patrykdankowski.financeflock.budgetgroup.dto.EmailDto;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupFacadePort;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupQueryServicePort;
import com.patrykdankowski.financeflock.user.dto.UserDto;
import com.patrykdankowski.financeflock.user.dto.UserLightDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class BudgetGroupControllerAdapterTest {

    @Mock
    private BudgetGroupFacadePort budgetGroupFacade;

    @Mock
    private BudgetGroupQueryServicePort budgetGroupQueryService;

    @InjectMocks
    private BudgetGroupControllerAdapter budgetGroupControllerAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBudgetGroup_shouldReturnSuccessMessage_whenCreationIsSuccessful() {
        BudgetGroupDto budgetGroupDto = new BudgetGroupDto("Valid Description");
        Long groupId = 1L;

        when(budgetGroupFacade.createBudgetGroup(any(BudgetGroupDto.class))).thenReturn(groupId);

        String response = budgetGroupControllerAdapter.createBudgetGroup(budgetGroupDto);

        assertThat(response).isEqualTo("Budget group created with id 1");
        verify(budgetGroupFacade, times(1)).createBudgetGroup(any(BudgetGroupDto.class));
    }

    @Test
    void deleteBudgetGroup_shouldCallFacadeToDeleteGroup() {
        Long groupId = 1L;

        budgetGroupControllerAdapter.deleteBudgetGroup(groupId);

        verify(budgetGroupFacade, times(1)).closeBudgetGroup(groupId);
    }

    @Test
    void addUserToGroup_shouldReturnSuccessMessage_whenUserIsAddedSuccessfully() {
        Long groupId = 1L;
        EmailDto emailDto = new EmailDto("user@example.com");

        doNothing().when(budgetGroupFacade).addUserToGroup(any(EmailDto.class), eq(groupId));

        String response = budgetGroupControllerAdapter.addUserToGroup(groupId, emailDto);

        assertThat(response).isEqualTo("User successfully added to group");
        verify(budgetGroupFacade, times(1)).addUserToGroup(any(EmailDto.class), eq(groupId));
    }

    @Test
    void removeUserFromGroup_shouldReturnSuccessMessage_whenUserIsRemovedSuccessfully() {
        Long groupId = 1L;
        EmailDto emailDto = new EmailDto("user@example.com");

        doNothing().when(budgetGroupFacade).removeUserFromGroup(any(EmailDto.class), eq(groupId));

        String response = budgetGroupControllerAdapter.removeUserFromGroup(groupId, emailDto);

        assertThat(response).isEqualTo("User successfully removed from group");
        verify(budgetGroupFacade, times(1)).removeUserFromGroup(any(EmailDto.class), eq(groupId));
    }

    @Test
    void listOfMembers_shouldReturnListOfUserLightDtos() {
        Long groupId = 1L;
        List<UserLightDto> userList = List.of(
                new UserLightDto("User1", "2024-10-06T10:00:00"),
                new UserLightDto("User2", "2024-10-06T11:00:00")
        );

        when(budgetGroupQueryService.listOfUsersInGroup(eq(groupId), anyInt(), anyInt(), anyString(), anyString()))
                .thenReturn(userList);

        List<UserLightDto> result = budgetGroupControllerAdapter.listOfMembers(groupId, 0, 5, "name", "asc");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("User1");
        assertThat(result.get(1).name()).isEqualTo("User2");
        verify(budgetGroupQueryService, times(1)).listOfUsersInGroup(eq(groupId), anyInt(), anyInt(), anyString(), anyString());
    }

    @Test
    void listOfExpansesInGroup_shouldReturnListOfUserDtos() {
        Long groupId = 1L;
        List<UserDto> userDtoList = List.of(
                new UserDto("User1", List.of(), BigDecimal.valueOf(100.00)),
                new UserDto("User2", List.of(), BigDecimal.valueOf(200.00))
        );

        when(budgetGroupQueryService.getBudgetGroupExpenses(eq(groupId), anyInt(), anyInt(), anyString()))
                .thenReturn(userDtoList);

        List<UserDto> result = budgetGroupControllerAdapter.listOfExpansesInGroup(groupId, 0, 5, "asc");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("User1");
        assertThat(result.get(1).getName()).isEqualTo("User2");
        verify(budgetGroupQueryService, times(1)).getBudgetGroupExpenses(eq(groupId), anyInt(), anyInt(), anyString());
    }

    @Test
    void createBudgetGroup_shouldThrowException_whenBudgetGroupDtoIsInvalid() {
        BudgetGroupDto budgetGroupDto = new BudgetGroupDto("");

        when(budgetGroupFacade.createBudgetGroup(any(BudgetGroupDto.class)))
                .thenThrow(new IllegalArgumentException("Invalid budget group description"));

        assertThatThrownBy(() -> budgetGroupControllerAdapter.createBudgetGroup(budgetGroupDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid budget group description");
    }

    @Test
    void deleteBudgetGroup_shouldThrowException_whenGroupIdIsInvalid() {
        Long groupId = -1L;

        doThrow(new IllegalArgumentException("Invalid group ID"))
                .when(budgetGroupFacade).closeBudgetGroup(groupId);

        assertThatThrownBy(() -> budgetGroupControllerAdapter.deleteBudgetGroup(groupId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid group ID");
    }

    @Test
    void addUserToGroup_shouldThrowException_whenEmailIsInvalid() {
        Long groupId = 1L;
        EmailDto emailDto = new EmailDto("invalid-email");

        doThrow(new IllegalArgumentException("Invalid email format"))
                .when(budgetGroupFacade).addUserToGroup(any(EmailDto.class), eq(groupId));

        assertThatThrownBy(() -> budgetGroupControllerAdapter.addUserToGroup(groupId, emailDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid email format");
    }

    @Test
    void removeUserFromGroup_shouldThrowException_whenUserNotFound() {
        Long groupId = 1L;
        EmailDto emailDto = new EmailDto("user@example.com");

        doThrow(new IllegalArgumentException("User not found in the group"))
                .when(budgetGroupFacade).removeUserFromGroup(any(EmailDto.class), eq(groupId));

        assertThatThrownBy(() -> budgetGroupControllerAdapter.removeUserFromGroup(groupId, emailDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found in the group");
    }

    @Test
    void listOfMembers_shouldThrowException_whenGroupIdIsInvalid() {
        Long groupId = -1L;

        when(budgetGroupQueryService.listOfUsersInGroup(eq(groupId), anyInt(), anyInt(), anyString(), anyString()))
                .thenThrow(new IllegalArgumentException("Invalid group ID"));

        assertThatThrownBy(() -> budgetGroupControllerAdapter.listOfMembers(groupId, 0, 5, "name", "asc"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid group ID");
    }

    @Test
    void listOfExpansesInGroup_shouldThrowException_whenGroupIdIsInvalid() {
        Long groupId = -1L;

        when(budgetGroupQueryService.getBudgetGroupExpenses(eq(groupId), anyInt(), anyInt(), anyString()))
                .thenThrow(new IllegalArgumentException("Invalid group ID"));

        assertThatThrownBy(() -> budgetGroupControllerAdapter.listOfExpansesInGroup(groupId, 0, 5, "asc"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid group ID");
    }

    @Test
    void listOfExpansesInGroup_shouldReturnEmptyList_whenNoExpensesFound() {
        Long groupId = 1L;

        when(budgetGroupQueryService.getBudgetGroupExpenses(eq(groupId), anyInt(), anyInt(), anyString()))
                .thenReturn(List.of());

        List<UserDto> result = budgetGroupControllerAdapter.listOfExpansesInGroup(groupId, 0, 5, "asc");

        assertThat(result).isEmpty();
        verify(budgetGroupQueryService, times(1)).getBudgetGroupExpenses(eq(groupId), anyInt(), anyInt(), anyString());
    }
}