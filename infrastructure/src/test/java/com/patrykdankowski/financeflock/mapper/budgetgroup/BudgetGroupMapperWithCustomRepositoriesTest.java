package com.patrykdankowski.financeflock.mapper.budgetgroup;

import com.patrykdankowski.financeflock.budgetgroup.entity.BudgetGroupSqlEntity;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.user.adapter.InMemoryUserQueryRepository;
import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class BudgetGroupMapperWithCustomRepositoriesTest {

    private InMemoryUserQueryRepository inMemoryUserQueryRepository;
    private BudgetGroupMapperInMemory budgetGroupMapper;

    @BeforeEach
    void setUp() {
        inMemoryUserQueryRepository = new InMemoryUserQueryRepository();
        budgetGroupMapper = new BudgetGroupMapperInMemory(inMemoryUserQueryRepository);
    }

    @Test
    void toSqlEntity_shouldMapDomainEntityToSqlEntity() {
        Long groupId = 1L;
        Long ownerId = 100L;
        Set<Long> memberIds = new HashSet<>(Arrays.asList(101L, 102L));

        UserSqlEntity owner = new UserSqlEntity();
        owner.setId(ownerId);
        inMemoryUserQueryRepository.save(owner);

        UserSqlEntity member1 = new UserSqlEntity();
        member1.setId(101L);
        inMemoryUserQueryRepository.save(member1);

        UserSqlEntity member2 = new UserSqlEntity();
        member2.setId(102L);
        inMemoryUserQueryRepository.save(member2);

        BudgetGroupDomainEntity domainEntity = BudgetGroupDomainEntity.buildBudgetGroup(groupId, "Test Group", ownerId);
        memberIds.forEach(domainEntity::addUser);

        BudgetGroupSqlEntity result = budgetGroupMapper.toSqlEntity(domainEntity);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(groupId);
        assertThat(result.getDescription()).isEqualTo("Test Group");
        assertThat(result.getOwner()).isEqualTo(owner);
        assertThat(result.getListOfMembers()).containsExactlyInAnyOrder(member1, member2);
    }

    @Test
    void toSqlEntity_shouldReturnNull_whenDomainEntityIsNull() {
        BudgetGroupSqlEntity result = budgetGroupMapper.toSqlEntity(null);

        assertThat(result).isNull();
    }

    @Test
    void toDomainEntity_shouldMapSqlEntityToDomainEntity() {
        Long groupId = 1L;
        Long ownerId = 100L;

        UserSqlEntity owner = new UserSqlEntity();
        owner.setId(ownerId);
        inMemoryUserQueryRepository.save(owner);

        UserSqlEntity member1 = new UserSqlEntity();
        member1.setId(101L);
        inMemoryUserQueryRepository.save(member1);

        UserSqlEntity member2 = new UserSqlEntity();
        member2.setId(102L);
        inMemoryUserQueryRepository.save(member2);

        BudgetGroupSqlEntity budgetGroupSql = new BudgetGroupSqlEntity();
        budgetGroupSql.setId(groupId);
        budgetGroupSql.setDescription("Test Group");
        budgetGroupSql.setOwner(owner);
        budgetGroupSql.setListOfMembers(new HashSet<>(Arrays.asList(member1, member2)));

        BudgetGroupDomainEntity result = budgetGroupMapper.toDomainEntity(budgetGroupSql);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(groupId);
        assertThat(result.getDescription()).isEqualTo("Test Group");
        assertThat(result.getOwnerId()).isEqualTo(ownerId);
        assertThat(result.getListOfMembersId()).containsExactlyInAnyOrder(101L, 102L);
    }

    @Test
    void toDomainEntity_shouldReturnNull_whenSqlEntityIsNull() {
        BudgetGroupDomainEntity result = budgetGroupMapper.toDomainEntity(null);

        assertThat(result).isNull();
    }
}
