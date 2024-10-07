package com.patrykdankowski.financeflock.budgetgroup.model.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
class BudgetGroupDomainEntityTest {

    private BudgetGroupDomainEntity budgetGroup;

    @BeforeEach
    void setUp() {
        budgetGroup = BudgetGroupDomainEntity.buildBudgetGroup(1L, "Initial Budget Group", 100L);
    }


    @Test
    void whenBuildBudgetGroup_thenAllFieldsAreInitializedCorrectly() {
        // given & when are covered by the setup

        // then
        assertThat(budgetGroup.getId()).isEqualTo(1L);
        assertThat(budgetGroup.getDescription()).isEqualTo("Initial Budget Group");
        assertThat(budgetGroup.getOwnerId()).isEqualTo(100L);
        assertThat(budgetGroup.getListOfMembersId()).isEmpty();
    }


    @Test
    void givenValidDescription_whenUpdateInfo_thenDescriptionIsUpdated() {
        // given
        String newDescription = "Updated Budget Group Description";

        // when
        budgetGroup.updateInfo(newDescription);

        // then
        assertThat(budgetGroup.getDescription()).isEqualTo(newDescription);
    }

    @Test
    void givenInvalidDescription_whenUpdateInfo_thenDescriptionIsNotUpdated() {
        // given
        String invalidDescription = "";

        // when
        budgetGroup.updateInfo(invalidDescription);

        // then
        assertThat(budgetGroup.getDescription()).isEqualTo("Initial Budget Group");
    }


    @Test
    void givenValidListOfIds_whenUpdateListOfMembers_thenMembersAreAdded() {
        // given
        Set<Long> newMembers = Set.of(200L, 300L, -1L);

        // when
        budgetGroup.updateListOfMembers(newMembers);

        // then
        assertThat(budgetGroup.getListOfMembersId()).contains(200L, 300L);
        assertThat(budgetGroup.getListOfMembersId()).doesNotContain(-1L);
    }

    @Test
    void givenValidUserId_whenAddUser_thenUserIsAdded() {
        // given
        Long userId = 200L;

        // when
        budgetGroup.addUser(userId);

        // then
        assertThat(budgetGroup.getListOfMembersId()).contains(userId);
    }

    @Test
    void givenInvalidUserId_whenAddUser_thenUserIsNotAdded() {
        // given
        Long invalidUserId = -5L;

        // when
        budgetGroup.addUser(invalidUserId);

        // then
        assertThat(budgetGroup.getListOfMembersId()).doesNotContain(invalidUserId);
    }

    @Test
    void givenExistingUserId_whenAddUser_thenUserIsNotDuplicated() {
        // given
        Long userId = 200L;
        budgetGroup.addUser(userId);

        // when
        budgetGroup.addUser(userId);

        // then
        assertThat(budgetGroup.getListOfMembersId()).containsOnlyOnce(userId);
    }

    @Test
    void givenValidUserId_whenRemoveUser_thenUserIsRemoved() {
        // given
        Long userId = 200L;
        budgetGroup.addUser(userId);

        // when
        budgetGroup.removeUser(userId);

        // then
        assertThat(budgetGroup.getListOfMembersId()).doesNotContain(userId);
    }

    @Test
    void givenInvalidUserId_whenRemoveUser_thenNoActionIsTaken() {
        // given
        Long userId = 200L;
        budgetGroup.addUser(userId);

        Long invalidUserId = -5L;

        // when
        budgetGroup.removeUser(invalidUserId);

        // then
        assertThat(budgetGroup.getListOfMembersId()).contains(userId);
    }

    @Test
    void givenNonExistentUserId_whenRemoveUser_thenNoActionIsTaken() {
        // given
        Long userId = 200L;

        // when
        budgetGroup.removeUser(userId);

        // then
        assertThat(budgetGroup.getListOfMembersId()).isEmpty();
    }

    @Test
    void whenUpdateListOfMembersWithEmptyList_thenNoMemberIsAdded() {
        // given
        Set<Long> emptyList = new HashSet<>();

        // when
        budgetGroup.updateListOfMembers(emptyList);

        // then
        assertThat(budgetGroup.getListOfMembersId()).isEmpty();
    }

    @Test
    void whenUpdateListOfMembersWithValidAndInvalidIds_thenOnlyValidAreAdded() {
        // given
        Set<Long> listOfIds = Set.of(10L, 20L, -10L);

        // when
        budgetGroup.updateListOfMembers(listOfIds);

        // then
        assertThat(budgetGroup.getListOfMembersId()).contains(10L, 20L);
        assertThat(budgetGroup.getListOfMembersId()).doesNotContain(-10L);
    }
    @Test
    void whenBuildBudgetGroupWithInvalidValues_thenBudgetGroupIsCreatedWithDefaults() {
        // given
        Long invalidId = -1L;
        String description = null;
        Long ownerId = -10L;

        // when
        BudgetGroupDomainEntity invalidBudgetGroup = BudgetGroupDomainEntity.buildBudgetGroup(invalidId, description, ownerId);

        // then
        assertThat(invalidBudgetGroup.getId()).isEqualTo(invalidId);
        assertThat(invalidBudgetGroup.getDescription()).isEqualTo(description);
        assertThat(invalidBudgetGroup.getOwnerId()).isEqualTo(ownerId);
    }

    @Test
    void givenNullDescription_whenUpdateInfo_thenDescriptionIsNotUpdated() {
        // given
        String nullDescription = null;

        // when
        budgetGroup.updateInfo(nullDescription);

        // then
        assertThat(budgetGroup.getDescription()).isEqualTo("Initial Budget Group");
    }
    @Test
    void whenUpdateListOfMembersWithNull_thenNoActionIsTaken() {
        // given
        Set<Long> nullList = null;

        // when
        budgetGroup.updateListOfMembers(nullList);

        // then
        assertThat(budgetGroup.getListOfMembersId()).isEmpty();
    }
    @Test
    void givenNullUserId_whenRemoveUser_thenNoActionIsTaken() {
        // given
        Long nullUserId = null;

        // when
        budgetGroup.removeUser(nullUserId);

        // then
        assertThat(budgetGroup.getListOfMembersId()).isEmpty();
    }

    @Test
    void givenLongDescription_whenUpdateInfo_thenDescriptionIsUpdated() {
        // given
        String longDescription = "This is a very long description that exceeds usual expectations for a description. It should still be valid unless there's a length restriction.";

        // when
        budgetGroup.updateInfo(longDescription);

        // then
        assertThat(budgetGroup.getDescription()).isEqualTo(longDescription);
    }

}