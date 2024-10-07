package com.patrykdankowski.financeflock.user.adapter;

import com.patrykdankowski.financeflock.user.exception.ToEarlyShareDataPreferenceException;
import com.patrykdankowski.financeflock.user.port.UserFacadePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserControllerAdapterTest {

    @Mock
    private UserFacadePort userFacade;

    @InjectMocks
    private UserControllerAdapter userControllerAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void leaveBudgetGroup_shouldReturnSuccessMessage_whenLeavingSuccessfully() {
        Long groupId = 1L;

        String response = userControllerAdapter.leaveBudgetGroup(groupId);

        assertThat(response).isEqualTo("Successfully left group");
        verify(userFacade, times(1)).leaveBudgetGroup(groupId);
    }

    @Test
    void updateShareDataPreference_shouldReturnSuccessMessage_whenSharingEnabled() {
        when(userFacade.toggleShareData()).thenReturn(true);

        String response = userControllerAdapter.updateShareDataPreference();

        assertThat(response).isEqualTo("You are now sharing your data");
        verify(userFacade, times(1)).toggleShareData();
    }

    @Test
    void updateShareDataPreference_shouldReturnSuccessMessage_whenSharingDisabled() {
        when(userFacade.toggleShareData()).thenReturn(false);

        String response = userControllerAdapter.updateShareDataPreference();

        assertThat(response).isEqualTo("You are not sharing your data now");
        verify(userFacade, times(1)).toggleShareData();
    }


    @Test
    void leaveBudgetGroup_shouldThrowException_whenGroupDoesNotExist() {
        Long groupId = 1L;
        doThrow(new RuntimeException("Group not found")).when(userFacade).leaveBudgetGroup(groupId);

        assertThatThrownBy(() -> userControllerAdapter.leaveBudgetGroup(groupId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Group not found");

        verify(userFacade, times(1)).leaveBudgetGroup(groupId);
    }

    @Test
    void leaveBudgetGroup_shouldThrowException_whenUserHasNoAccess() {
        Long groupId = 2L;
        doThrow(new SecurityException("User does not have access to leave this group"))
                .when(userFacade).leaveBudgetGroup(groupId);

        assertThatThrownBy(() -> userControllerAdapter.leaveBudgetGroup(groupId))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("User does not have access to leave this group");

        verify(userFacade, times(1)).leaveBudgetGroup(groupId);
    }

    @Test
    void updateShareDataPreference_shouldThrowException_whenToggleFails() {
        LocalDateTime lastSharedData = LocalDateTime.now().minusMinutes(3);
        ToEarlyShareDataPreferenceException exception = new ToEarlyShareDataPreferenceException(lastSharedData);

        doThrow(exception).when(userFacade).toggleShareData();

        assertThatThrownBy(() -> userControllerAdapter.updateShareDataPreference())
                .isInstanceOf(ToEarlyShareDataPreferenceException.class)
                .hasMessageContaining("Cannot toggle sharing preference before 5 minutes have passed since: " + lastSharedData.toString())
                .extracting(e -> ((ToEarlyShareDataPreferenceException) e).getNextPossibleShareData())
                .isEqualTo(lastSharedData.plusMinutes(5));

        verify(userFacade, times(1)).toggleShareData();
    }

    @Test
    void updateShareDataPreference_shouldThrowException_whenUserIsNotAuthorized() {
        doThrow(new SecurityException("User is not authorized to perform this action"))
                .when(userFacade).toggleShareData();

        assertThatThrownBy(() -> userControllerAdapter.updateShareDataPreference())
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("User is not authorized to perform this action");

        verify(userFacade, times(1)).toggleShareData();
    }

    @Test
    void leaveBudgetGroup_shouldThrowException_whenUserIsNotLoggedIn() {
        Long groupId = 3L;

        doThrow(new SecurityException("User is not logged in"))
                .when(userFacade).leaveBudgetGroup(groupId);

        assertThatThrownBy(() -> userControllerAdapter.leaveBudgetGroup(groupId))
                .isInstanceOf(SecurityException.class)
                .hasMessageContaining("User is not logged in");

        verify(userFacade, times(1)).leaveBudgetGroup(groupId);
    }


    @Test
    void updateShareDataPreference_shouldLogAttemptToUpdatePreference() {
        when(userFacade.toggleShareData()).thenReturn(true);

        userControllerAdapter.updateShareDataPreference();

        verify(userFacade, times(1)).toggleShareData();
    }

    @Test
    void leaveBudgetGroup_shouldLogAttemptToLeaveGroup() {
        Long groupId = 4L;

        userControllerAdapter.leaveBudgetGroup(groupId);

        verify(userFacade, times(1)).leaveBudgetGroup(groupId);
    }
}

