package com.patrykdankowski.financeflock.user.adapter;

import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryUserCommandRepositoryTest {

    private InMemoryUserCommandRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserCommandRepository();
    }

    @Test
    void save_shouldPersistUser_whenNewUserIsSaved() {
        UserSqlEntity user = createUser(null, "john.doe@example.com");

        UserSqlEntity savedUser = repository.save(user);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(repository.findById(savedUser.getId())).isPresent();
    }

    @Test
    void save_shouldUpdateUser_whenUserWithSameIdIsSavedAgain() {
        UserSqlEntity user = createUser(null, "john.doe@example.com");
        UserSqlEntity savedUser = repository.save(user);

        savedUser.setEmail("john.updated@example.com");

        UserSqlEntity updatedUser = repository.save(savedUser);

        assertThat(updatedUser.getEmail()).isEqualTo("john.updated@example.com");
        assertThat(repository.findById(updatedUser.getId()).get().getEmail()).isEqualTo("john.updated@example.com");
    }

    @Test
    void findByEmail_shouldReturnUser_whenUserWithGivenEmailExists() {
        UserSqlEntity user = createUser(null, "jane.doe@example.com");
        repository.save(user);

        Optional<UserSqlEntity> foundUser = repository.findByEmail("jane.doe@example.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("jane.doe@example.com");
    }

    @Test
    void findByEmail_shouldReturnEmpty_whenUserWithGivenEmailDoesNotExist() {
        Optional<UserSqlEntity> foundUser = repository.findByEmail("not.existing@example.com");

        assertThat(foundUser).isNotPresent();
    }

    @Test
    void findAllByIdIn_shouldReturnUsers_whenUsersWithGivenIdsExist() {
        UserSqlEntity user1 = repository.save(createUser(null, "user1@example.com"));
        UserSqlEntity user2 = repository.save(createUser(null, "user2@example.com"));

        List<UserSqlEntity> users = repository.findAllByIdIn(List.of(user1.getId(), user2.getId()));

        assertThat(users).hasSize(2);
        assertThat(users).extracting("email").contains("user1@example.com", "user2@example.com");
    }

    @Test
    void findAllByIdIn_shouldReturnEmptyList_whenNoUsersWithGivenIdsExist() {
        List<UserSqlEntity> users = repository.findAllByIdIn(List.of(99L, 100L));

        assertThat(users).isEmpty();
    }

    @Test
    void existsUserByEmail_shouldReturnTrue_whenUserWithEmailExists() {
        UserSqlEntity user = createUser(null, "existing.user@example.com");
        repository.save(user);

        boolean exists = repository.existsUserByEmail("existing.user@example.com");

        assertThat(exists).isTrue();
    }

    @Test
    void existsUserByEmail_shouldReturnFalse_whenUserWithEmailDoesNotExist() {
        boolean exists = repository.existsUserByEmail("non.existent.user@example.com");

        assertThat(exists).isFalse();
    }

    @Test
    void updateLastLoggedInAt_shouldUpdateLastLoggedInTime_whenUserWithEmailExists() {
        UserSqlEntity user = createUser(null, "login.update@example.com");
        repository.save(user);
        LocalDateTime newLastLoggedInAt = LocalDateTime.now().minusDays(1);

        repository.updateLastLoggedInAt(newLastLoggedInAt, "login.update@example.com");

        Optional<UserSqlEntity> updatedUser = repository.findByEmail("login.update@example.com");
        assertThat(updatedUser).isPresent();
        assertThat(updatedUser.get().getLastLoggedInAt()).isEqualTo(newLastLoggedInAt);
    }

    @Test
    void updateLastLoggedInAt_shouldDoNothing_whenUserWithEmailDoesNotExist() {
        repository.updateLastLoggedInAt(LocalDateTime.now(), "non.existent@example.com");

        Optional<UserSqlEntity> user = repository.findByEmail("non.existent@example.com");
        assertThat(user).isNotPresent();
    }

    @Test
    void findById_shouldReturnUser_whenUserWithGivenIdExists() {
        UserSqlEntity user = createUser(null, "findbyid@example.com");
        UserSqlEntity savedUser = repository.save(user);

        Optional<UserSqlEntity> foundUser = repository.findById(savedUser.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("findbyid@example.com");
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenUserWithGivenIdDoesNotExist() {
        Optional<UserSqlEntity> foundUser = repository.findById(99L);

        assertThat(foundUser).isNotPresent();
    }

    private UserSqlEntity createUser(Long id, String email) {
        UserSqlEntity user = new UserSqlEntity();
        user.setId(id);
        user.setEmail(email);
        user.setName("Test User");
        user.setPassword("password");
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }
}
