package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.budgetgroup.entity.BudgetGroupSqlEntity;
import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class InMemoryBudgetGroupQueryRepositoryTest {

    private InMemoryBudgetGroupQueryRepository inMemoryBudgetGroupQueryRepository;

    @BeforeEach
    void setUp() {
        inMemoryBudgetGroupQueryRepository = new InMemoryBudgetGroupQueryRepository();
    }

    @Test
    void findById_shouldReturnBudgetGroupSqlEntity_whenGroupExists() {
        Long groupId = 1L;
        String description = "Test Group";

        UserSqlEntity owner = new UserSqlEntity();
        owner.setId(100L);
        owner.setEmail("owner@example.com");

        Set<UserSqlEntity> members = new HashSet<>();
        UserSqlEntity member1 = new UserSqlEntity();
        member1.setId(101L);
        member1.setEmail("member1@example.com");
        members.add(member1);

        UserSqlEntity member2 = new UserSqlEntity();
        member2.setId(102L);
        member2.setEmail("member2@example.com");
        members.add(member2);

        BudgetGroupSqlEntity sqlEntity = new BudgetGroupSqlEntity();
        sqlEntity.setId(groupId);
        sqlEntity.setDescription(description);
        sqlEntity.setOwner(owner);
        sqlEntity.setListOfMembers(members);

        inMemoryBudgetGroupQueryRepository.save(sqlEntity);

        Optional<BudgetGroupSqlEntity> result = inMemoryBudgetGroupQueryRepository.findById(groupId);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(groupId);
        assertThat(result.get().getDescription()).isEqualTo(description);
        assertThat(result.get().getOwner()).isEqualTo(owner);
        assertThat(result.get().getListOfMembers()).isEqualTo(members);
    }

    @Test
    void findById_shouldReturnEmptyOptional_whenGroupDoesNotExist() {
        Long nonExistentGroupId = 99L;

        Optional<BudgetGroupSqlEntity> result = inMemoryBudgetGroupQueryRepository.findById(nonExistentGroupId);

        assertThat(result).isNotPresent();
    }

}
