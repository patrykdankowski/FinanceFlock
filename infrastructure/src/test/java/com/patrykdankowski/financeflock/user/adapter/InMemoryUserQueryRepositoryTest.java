package com.patrykdankowski.financeflock.user.adapter;

import com.patrykdankowski.financeflock.budgetgroup.entity.BudgetGroupSqlEntity;
import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryUserQueryRepositoryTest {

    private InMemoryUserQueryRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryUserQueryRepository();
    }

    @Test
    void save_shouldPersistUserSqlEntity() {
        UserSqlEntity user = createUser(null, "johndoe@example.com");

        UserSqlEntity savedUser = repository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("johndoe@example.com");
    }

    @Test
    void findAllByBudgetGroup_Id_shouldReturnUsersForGivenGroup() {
        BudgetGroupSqlEntity group = createBudgetGroup(1L);

        UserSqlEntity user1 = createUser(null, "user1@example.com");
        user1.setBudgetGroup(group);
        UserSqlEntity user2 = createUser(null, "user2@example.com");
        user2.setBudgetGroup(group);

        repository.save(user1);
        repository.save(user2);

        List<UserSqlEntity> users = repository.findAllByBudgetGroup_Id(1L, 0, 10);

        assertThat(users).hasSize(2);
        assertThat(users).extracting(UserSqlEntity::getEmail).contains("user1@example.com", "user2@example.com");
    }

    @Test
    void findSimpleUserByEmail_shouldReturnUser_whenUserExists() {
        UserSqlEntity user = createUser(null, "testuser@example.com");
        repository.save(user);

        Optional<UserSqlEntity> foundUser = repository.findSimpleUserByEmail("testuser@example.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("testuser@example.com");
    }

    @Test
    void findSimpleUserByEmail_shouldReturnEmpty_whenUserDoesNotExist() {
        Optional<UserSqlEntity> foundUser = repository.findSimpleUserByEmail("nonexistent@example.com");

        assertThat(foundUser).isNotPresent();
    }

    @Test
    void save_shouldUpdateUser_whenUserWithSameIdIsSavedAgain() {
        Long userId = 1L;
        UserSqlEntity user = new UserSqlEntity();
        user.setId(userId);
        user.setEmail("user@example.com");

        repository.save(user);

        user.setEmail("updateduser@example.com");
        UserSqlEntity updatedUser = repository.save(user);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getEmail()).isEqualTo("updateduser@example.com");
        Optional<UserSqlEntity> foundUser = repository.findById(userId);
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("updateduser@example.com");
    }

    @Test
    void findAllByBudgetGroup_Id_shouldReturnEmptyList_whenNoUsersInGroup() {
        Long groupId = 1L;

        List<UserSqlEntity> users = repository.findAllByBudgetGroup_Id(groupId, 0, 10);

        assertThat(users).isEmpty();
    }

    @Test
    void findAllByBudgetGroup_Id_shouldReturnPaginatedResults() {
        BudgetGroupSqlEntity budgetGroup = new BudgetGroupSqlEntity();
        budgetGroup.setId(1L);

        for (long i = 1; i <= 20; i++) {
            UserSqlEntity user = new UserSqlEntity();
            user.setId(i);
            user.setEmail("user" + i + "@example.com");
            user.setBudgetGroup(budgetGroup);
            repository.save(user);
        }

        List<UserSqlEntity> usersPage1 = repository.findAllByBudgetGroup_Id(1L, 0, 10);

        assertThat(usersPage1).hasSize(10);
        assertThat(usersPage1.get(0).getEmail()).isEqualTo("user1@example.com");
        assertThat(usersPage1.get(9).getEmail()).isEqualTo("user10@example.com");
    }

    @Test
    void findAllByBudgetGroup_Id_shouldHandleOutOfBoundsPage() {
        BudgetGroupSqlEntity budgetGroup = new BudgetGroupSqlEntity();
        budgetGroup.setId(1L);

        for (long i = 1; i <= 5; i++) {
            UserSqlEntity user = new UserSqlEntity();
            user.setId(i);
            user.setEmail("user" + i + "@example.com");
            user.setBudgetGroup(budgetGroup);
            repository.save(user);
        }

        List<UserSqlEntity> usersOutOfBounds = repository.findAllByBudgetGroup_Id(1L, 10, 10);

        assertThat(usersOutOfBounds).isEmpty();
    }

    @Test
    void save_shouldPersistUserWithBudgetGroup() {
        Long groupId = 1L;
        BudgetGroupSqlEntity budgetGroup = new BudgetGroupSqlEntity();
        budgetGroup.setId(groupId);
        budgetGroup.setDescription("Budget Group");

        UserSqlEntity user = new UserSqlEntity();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setBudgetGroup(budgetGroup);

        UserSqlEntity savedUser = repository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getBudgetGroup()).isEqualTo(budgetGroup);
        Optional<UserSqlEntity> foundUser = repository.findById(1L);
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getBudgetGroup().getId()).isEqualTo(groupId);
    }

    private UserSqlEntity createUser(Long id, String email) {
        UserSqlEntity user = new UserSqlEntity();
        user.setId(id);
        user.setEmail(email);
        return user;
    }

    private BudgetGroupSqlEntity createBudgetGroup(Long id) {
        BudgetGroupSqlEntity group = new BudgetGroupSqlEntity();
        group.setId(id);
        return group;
    }
}
