package com.patrykdankowski.financeflock.user.adapter;

import com.patrykdankowski.financeflock.user.exception.UserAlreadyExistsException;
import com.patrykdankowski.financeflock.user.exception.UserNotFoundException;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.port.UserCommandRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserCommandServiceAdapterTest {

    @Mock
    private UserCommandRepositoryPort userCommandRepository;

    @InjectMocks
    private UserCommandServiceAdapter userCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void findUserByEmail_shouldReturnUser_whenUserExists() {
        String email = "test@example.com";
        UserDomainEntity user = createUser(null, email);
        when(userCommandRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDomainEntity foundUser = userCommandService.findUserByEmail(email);

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo(email);
        verify(userCommandRepository, times(1)).findByEmail(email);
    }

    @Test
    void findUserByEmail_shouldThrowUserNotFoundException_whenUserDoesNotExist() {
        String email = "notfound@example.com";
        when(userCommandRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userCommandService.findUserByEmail(email))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with email " + email + " not found");
        verify(userCommandRepository, times(1)).findByEmail(email);
    }


    @Test
    void checkIfUserExists_shouldThrowUserAlreadyExistsException_whenUserExists() {
        String email = "existing@example.com";
        when(userCommandRepository.existsUserByEmail(email)).thenReturn(true);

        assertThatThrownBy(() -> userCommandService.checkIfUserExists(email))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("User with email with email already exists");
        verify(userCommandRepository, times(1)).existsUserByEmail(email);
    }

    @Test
    void checkIfUserExists_shouldDoNothing_whenUserDoesNotExist() {
        String email = "newuser@example.com";
        when(userCommandRepository.existsUserByEmail(email)).thenReturn(false);

        userCommandService.checkIfUserExists(email);

        verify(userCommandRepository, times(1)).existsUserByEmail(email);
    }


    @Test
    void saveUser_shouldSaveAndReturnUser() {
        UserDomainEntity user = createUser(null, "saveuser@example.com");
        when(userCommandRepository.save(user)).thenReturn(user);

        UserDomainEntity savedUser = userCommandService.saveUser(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("saveuser@example.com");
        verify(userCommandRepository, times(1)).save(user);
    }


    @Test
    void saveAllUsers_shouldSaveAndReturnListOfUsers() {
        UserDomainEntity user1 = createUser(null, "user1@example.com");
        UserDomainEntity user2 = createUser(null, "user2@example.com");
        List<UserDomainEntity> users = List.of(user1, user2);

        when(userCommandRepository.saveAll(users)).thenReturn(users);

        List<UserDomainEntity> savedUsers = userCommandService.saveAllUsers(users);

        assertThat(savedUsers).hasSize(2);
        assertThat(savedUsers).extracting("email").contains("user1@example.com", "user2@example.com");
        verify(userCommandRepository, times(1)).saveAll(users);
    }


    @Test
    void listOfUsersFromIds_shouldReturnListOfUsers_whenUsersWithGivenIdsExist() {
        UserDomainEntity user1 = createUser(1L, "user1@example.com");
        UserDomainEntity user2 = createUser(2L, "user2@example.com");
        List<Long> userIds = List.of(1L, 2L);

        when(userCommandRepository.findAllByIdIn(userIds)).thenReturn(List.of(user1, user2));

        List<UserDomainEntity> users = userCommandService.listOfUsersFromIds(userIds);

        assertThat(users).hasSize(2);
        assertThat(users).extracting("email").contains("user1@example.com", "user2@example.com");
        verify(userCommandRepository, times(1)).findAllByIdIn(userIds);
    }


    @Test
    void updateLastLoggedInAt_shouldUpdateLastLoggedInTime_whenUserExists() {
        String email = "lastlogin@example.com";
        LocalDateTime newLastLoggedInAt = LocalDateTime.now().minusDays(1);

        doNothing().when(userCommandRepository).updateLastLoginDate(newLastLoggedInAt, email);

        userCommandService.updateLastLoggedInAt(newLastLoggedInAt, email);

        verify(userCommandRepository, times(1)).updateLastLoginDate(newLastLoggedInAt, email);
    }

    private UserDomainEntity createUser(Long id, String email) {
        return UserDomainEntity.buildUser(id, "Test User", "password", email, LocalDateTime.now());
    }
}