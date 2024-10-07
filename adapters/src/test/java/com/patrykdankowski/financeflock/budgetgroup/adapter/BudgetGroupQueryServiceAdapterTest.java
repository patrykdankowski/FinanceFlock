package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.auth.port.AuthenticationServicePort;
import com.patrykdankowski.financeflock.budgetgroup.exception.BudgetGroupNotFoundException;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupQueryRepositoryPort;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.dto.SimpleUserDomainEntity;
import com.patrykdankowski.financeflock.user.dto.UserDto;
import com.patrykdankowski.financeflock.user.dto.UserLightDto;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.port.UserQueryRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
class BudgetGroupQueryServiceAdapterTest {

    @Mock
    private AuthenticationServicePort authenticationService;

    @Mock
    private UserQueryRepositoryPort userQueryRepository;

    @Mock
    private BudgetGroupQueryRepositoryPort budgetGroupQueryRepository;

    @InjectMocks
    private BudgetGroupQueryServiceAdapter budgetGroupQueryServiceAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listOfUsersInGroup_shouldReturnListOfUserLightDtos_whenGroupExists() {
        SimpleUserDomainEntity loggedUser = SimpleUserDomainEntity.buildUser(
                1L, "Test User", "test@example.com", 1L, Role.GROUP_MEMBER,
                LocalDateTime.now(), true, LocalDateTime.now(), LocalDateTime.now());

        BudgetGroupDomainEntity budgetGroup = mock(BudgetGroupDomainEntity.class);
        List<UserLightDto> userLightDtos = List.of(
                new UserLightDto("User1", "2024-10-06T10:00:00"),
                new UserLightDto("User2", "2024-10-06T11:00:00")
        );

        when(authenticationService.getSimpleUserFromContext()).thenReturn(loggedUser);
        when(budgetGroupQueryRepository.findBudgetGroupById(1L)).thenReturn(Optional.of(budgetGroup));
        when(userQueryRepository.findAllByBudgetGroup_Id(anyLong(), any(Pageable.class))).thenReturn(userLightDtos);

        List<UserLightDto> result = budgetGroupQueryServiceAdapter.listOfUsersInGroup(1L, 0, 5, "name", "asc");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("User1");
        assertThat(result.get(1).name()).isEqualTo("User2");

        verify(authenticationService, times(1)).getSimpleUserFromContext();
        verify(budgetGroupQueryRepository, times(1)).findBudgetGroupById(1L);
        verify(userQueryRepository, times(1)).findAllByBudgetGroup_Id(anyLong(), any(Pageable.class));
    }

    @Test
    void listOfUsersInGroup_shouldThrowException_whenGroupDoesNotExist() {
        SimpleUserDomainEntity loggedUser = SimpleUserDomainEntity.buildUser(
                1L, "Test User", "test@example.com", 1L, Role.GROUP_MEMBER,
                LocalDateTime.now(), true, LocalDateTime.now(), LocalDateTime.now());

        when(authenticationService.getSimpleUserFromContext()).thenReturn(loggedUser);
        when(budgetGroupQueryRepository.findBudgetGroupById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> budgetGroupQueryServiceAdapter.listOfUsersInGroup(1L, 0, 5, "name", "asc"))
                .isInstanceOf(BudgetGroupNotFoundException.class)
                .hasMessage("Budget group with ID 1 not found");

        verify(authenticationService, times(1)).getSimpleUserFromContext();
        verify(budgetGroupQueryRepository, times(1)).findBudgetGroupById(1L);
        verify(userQueryRepository, never()).findAllByBudgetGroup_Id(anyLong(), any(Pageable.class));
    }

    @Test
    void getBudgetGroupExpenses_shouldReturnListOfUserDtos_whenGroupExists() {
        UserDomainEntity userFromContext = mock(UserDomainEntity.class);
        List<UserDto> userDtos = List.of(
                new UserDto("User1", List.of(), BigDecimal.valueOf(100.00)),
                new UserDto("User2", List.of(), BigDecimal.valueOf(200.00))
        );

        when(authenticationService.getFullUserFromContext()).thenReturn(userFromContext);
        when(userFromContext.getBudgetGroupId()).thenReturn(1L);
        when(userQueryRepository.findUserExpenseSummaries(eq(1L), any(Pageable.class))).thenReturn(userDtos);

        List<UserDto> result = budgetGroupQueryServiceAdapter.getBudgetGroupExpenses(1L, 0, 5, "asc");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("User1");
        assertThat(result.get(1).getName()).isEqualTo("User2");

        verify(authenticationService, times(1)).getFullUserFromContext();
        verify(userQueryRepository, times(1)).findUserExpenseSummaries(eq(1L), any(Pageable.class));
    }

    @Test
    void getBudgetGroupExpenses_shouldReturnEmptyList_whenGroupDoesNotExist() {
        UserDomainEntity userFromContext = mock(UserDomainEntity.class);
        when(authenticationService.getFullUserFromContext()).thenReturn(userFromContext);
        when(userFromContext.getBudgetGroupId()).thenReturn(null);

        List<UserDto> result = budgetGroupQueryServiceAdapter.getBudgetGroupExpenses(1L, 0, 5, "asc");

        assertThat(result).isEmpty();
        verify(authenticationService, times(1)).getFullUserFromContext();
        verify(userQueryRepository, never()).findUserExpenseSummaries(anyLong(), any(Pageable.class));
    }

    @Test
    void listOfUsersInGroup_shouldThrowException_whenUserIsNotLoggedIn() {
        when(authenticationService.getSimpleUserFromContext()).thenReturn(null);

        assertThatThrownBy(() -> budgetGroupQueryServiceAdapter.listOfUsersInGroup(1L, 0, 5, "name", "asc"))
                .isInstanceOf(NullPointerException.class);

        verify(authenticationService, times(1)).getSimpleUserFromContext();
        verify(budgetGroupQueryRepository, never()).findBudgetGroupById(anyLong());
        verify(userQueryRepository, never()).findAllByBudgetGroup_Id(anyLong(), any(Pageable.class));
    }

    @Test
    void listOfUsersInGroup_shouldReturnEmptyList_whenGroupExistsButNoUsers() {
        SimpleUserDomainEntity loggedUser = SimpleUserDomainEntity.buildUser(
                1L, "Test User", "test@example.com", 1L, Role.GROUP_MEMBER,
                LocalDateTime.now(), true, LocalDateTime.now(), LocalDateTime.now());

        BudgetGroupDomainEntity budgetGroup = mock(BudgetGroupDomainEntity.class);
        List<UserLightDto> emptyUserList = List.of();

        when(authenticationService.getSimpleUserFromContext()).thenReturn(loggedUser);
        when(budgetGroupQueryRepository.findBudgetGroupById(1L)).thenReturn(Optional.of(budgetGroup));
        when(userQueryRepository.findAllByBudgetGroup_Id(anyLong(), any(Pageable.class))).thenReturn(emptyUserList);

        List<UserLightDto> result = budgetGroupQueryServiceAdapter.listOfUsersInGroup(1L, 0, 5, "name", "asc");

        assertThat(result).isEmpty();
        verify(authenticationService, times(1)).getSimpleUserFromContext();
        verify(budgetGroupQueryRepository, times(1)).findBudgetGroupById(1L);
        verify(userQueryRepository, times(1)).findAllByBudgetGroup_Id(anyLong(), any(Pageable.class));
    }

    @Test
    void listOfUsersInGroup_shouldThrowException_whenPageIsNegative() {
        // Arrange
        SimpleUserDomainEntity loggedUser = SimpleUserDomainEntity.buildUser(
                1L, "Test User", "test@example.com", 1L, Role.GROUP_MEMBER,
                LocalDateTime.now(), true, LocalDateTime.now(), LocalDateTime.now());

        assertThatThrownBy(() -> budgetGroupQueryServiceAdapter.listOfUsersInGroup(1L, -1, 5, "name", "asc"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Page index must not be less than zero");

        verify(authenticationService, never()).getSimpleUserFromContext();
        verify(budgetGroupQueryRepository, never()).findBudgetGroupById(anyLong());
        verify(userQueryRepository, never()).findAllByBudgetGroup_Id(anyLong(), any(Pageable.class));
    }

    @Test
    void listOfUsersInGroup_shouldReturnExpectedUsers_whenInvoked() {
        SimpleUserDomainEntity loggedUser = SimpleUserDomainEntity.buildUser(
                1L, "Test User", "test@example.com", 1L, Role.GROUP_MEMBER,
                LocalDateTime.now(), true, LocalDateTime.now(), LocalDateTime.now());

        BudgetGroupDomainEntity budgetGroup = mock(BudgetGroupDomainEntity.class);
        List<UserLightDto> userLightDtos = List.of(
                new UserLightDto("User1", "2024-10-06T10:00:00"),
                new UserLightDto("User2", "2024-10-06T11:00:00")
        );

        when(authenticationService.getSimpleUserFromContext()).thenReturn(loggedUser);
        when(budgetGroupQueryRepository.findBudgetGroupById(1L)).thenReturn(Optional.of(budgetGroup));
        when(userQueryRepository.findAllByBudgetGroup_Id(anyLong(), any(Pageable.class))).thenReturn(userLightDtos);

        List<UserLightDto> result = budgetGroupQueryServiceAdapter.listOfUsersInGroup(1L, 0, 5, "name", "asc");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("User1");
        assertThat(result.get(1).name()).isEqualTo("User2");

        verify(authenticationService, times(1)).getSimpleUserFromContext();
        verify(budgetGroupQueryRepository, times(1)).findBudgetGroupById(1L);
        verify(userQueryRepository, times(1)).findAllByBudgetGroup_Id(anyLong(), any(Pageable.class));
    }


}