package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.budgetgroup.entity.BudgetGroupSqlEntity;
import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryBudgetGroupCommandRepositoryTest {

    private InMemoryBudgetGroupCommandRepository inMemoryBudgetGroupCommandRepository;

    @BeforeEach
    void setUp() {
        inMemoryBudgetGroupCommandRepository = new InMemoryBudgetGroupCommandRepository();
    }


    @Test
    void findById_shouldReturnBudgetGroupSqlEntity_whenGroupExists() {
        Long groupId = 1L;
        String description = "Test Group";

        UserSqlEntity owner = new UserSqlEntity();
        owner.setId(100L);
        owner.setEmail("owner@example.com");

        UserSqlEntity member1 = new UserSqlEntity();
        member1.setId(101L);
        member1.setEmail("member1@example.com");

        UserSqlEntity member2 = new UserSqlEntity();
        member2.setId(102L);
        member2.setEmail("member2@example.com");

        Set<UserSqlEntity> listOfMembers = new HashSet<>();
        listOfMembers.add(member1);
        listOfMembers.add(member2);

        BudgetGroupSqlEntity sqlEntity = new BudgetGroupSqlEntity();
        sqlEntity.setId(groupId);
        sqlEntity.setDescription(description);
        sqlEntity.setOwner(owner);
        sqlEntity.setListOfMembers(listOfMembers);

        inMemoryBudgetGroupCommandRepository.save(sqlEntity);

        Optional<BudgetGroupSqlEntity> result = inMemoryBudgetGroupCommandRepository.findById(groupId);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(groupId);
        assertThat(result.get().getDescription()).isEqualTo(description);
        assertThat(result.get().getOwner()).isEqualTo(owner);
        assertThat(result.get().getListOfMembers()).hasSize(2);
        assertThat(result.get().getListOfMembers()).containsExactlyInAnyOrder(member1, member2);
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenGroupDoesNotExist() {
        Long groupId = 99L;

        Optional<BudgetGroupSqlEntity> result = inMemoryBudgetGroupCommandRepository.findById(groupId);

        assertThat(result).isNotPresent();
    }


    @Test
    void save_shouldPersistBudgetGroupSqlEntity() {
        Long groupId = 1L;
        String description = "Test Group";

        UserSqlEntity owner = new UserSqlEntity();
        owner.setId(100L);
        owner.setEmail("owner@example.com");

        UserSqlEntity member1 = new UserSqlEntity();
        member1.setId(101L);
        member1.setEmail("member1@example.com");

        Set<UserSqlEntity> listOfMembers = new HashSet<>();
        listOfMembers.add(member1);

        BudgetGroupSqlEntity sqlEntity = new BudgetGroupSqlEntity();
        sqlEntity.setId(groupId);
        sqlEntity.setDescription(description);
        sqlEntity.setOwner(owner);
        sqlEntity.setListOfMembers(listOfMembers);

        BudgetGroupSqlEntity result = inMemoryBudgetGroupCommandRepository.save(sqlEntity);

        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.getOwner()).isEqualTo(owner);
        assertThat(result.getListOfMembers()).hasSize(1);
        assertThat(result.getListOfMembers()).containsExactly(member1);
    }

    @Test
    void save_shouldUpdateExistingBudgetGroup_whenIdExists() {
        Long groupId = 1L;
        String description = "Test Group";

        UserSqlEntity owner = new UserSqlEntity();
        owner.setId(100L);
        owner.setEmail("owner@example.com");

        BudgetGroupSqlEntity sqlEntity = new BudgetGroupSqlEntity();
        sqlEntity.setId(groupId);
        sqlEntity.setDescription(description);
        sqlEntity.setOwner(owner);

        inMemoryBudgetGroupCommandRepository.save(sqlEntity);

        UserSqlEntity member1 = new UserSqlEntity();
        member1.setId(101L);
        member1.setEmail("member1@example.com");

        UserSqlEntity member2 = new UserSqlEntity();
        member2.setId(102L);
        member2.setEmail("member2@example.com");

        Set<UserSqlEntity> updatedMembers = new HashSet<>();
        updatedMembers.add(member1);
        updatedMembers.add(member2);
        sqlEntity.setListOfMembers(updatedMembers);

        BudgetGroupSqlEntity result = inMemoryBudgetGroupCommandRepository.save(sqlEntity);

        assertThat(result).isNotNull();
        assertThat(result.getListOfMembers()).hasSize(2);
        assertThat(result.getListOfMembers()).containsExactlyInAnyOrder(member1, member2);
    }


    @Test
    void delete_shouldRemoveBudgetGroupSqlEntity() {
        Long groupId = 1L;
        String description = "Test Group";

        UserSqlEntity owner = new UserSqlEntity();
        owner.setId(100L);
        owner.setEmail("owner@example.com");

        UserSqlEntity member1 = new UserSqlEntity();
        member1.setId(101L);
        member1.setEmail("member1@example.com");

        Set<UserSqlEntity> listOfMembers = new HashSet<>();
        listOfMembers.add(member1);

        BudgetGroupSqlEntity sqlEntity = new BudgetGroupSqlEntity();
        sqlEntity.setId(groupId);
        sqlEntity.setDescription(description);
        sqlEntity.setOwner(owner);
        sqlEntity.setListOfMembers(listOfMembers);

        inMemoryBudgetGroupCommandRepository.save(sqlEntity);

        inMemoryBudgetGroupCommandRepository.delete(sqlEntity);

        Optional<BudgetGroupSqlEntity> result = inMemoryBudgetGroupCommandRepository.findById(groupId);
        assertThat(result).isNotPresent();
    }
}
