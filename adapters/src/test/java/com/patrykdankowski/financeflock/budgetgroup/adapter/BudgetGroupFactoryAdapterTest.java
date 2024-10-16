package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.model.record.BudgetGroupDescription;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupFactoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BudgetGroupFactoryAdapterTest {

    private BudgetGroupFactoryPort budgetGroupFactoryAdapter;

    @BeforeEach
    void setUp() {
        budgetGroupFactoryAdapter = new BudgetGroupFactoryAdapter();
    }

    @Test
    void createBudgetGroupFromRequest_shouldCreateBudgetGroupWithCorrectAdminUserId() {
        Long userId = 1L;
        BudgetGroupDescription description = new BudgetGroupDescription("Test Budget Group");

        BudgetGroupDomainEntity result = budgetGroupFactoryAdapter.createBudgetGroupFromRequest(userId, description);

        assertThat(result).isNotNull();
        assertThat(result.getOwnerId()).isEqualTo(userId);
    }

    @Test
    void createBudgetGroupFromRequest_shouldSetBudgetGroupDescriptionCorrectly() {
        Long userId = 1L;
        BudgetGroupDescription description = new BudgetGroupDescription("Sample Group Description");

        BudgetGroupDomainEntity result = budgetGroupFactoryAdapter.createBudgetGroupFromRequest(userId, description);

        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo("Sample Group Description");
    }

    @Test
    void createBudgetGroupFromRequest_shouldAddUserToMembersList() {
        Long userId = 1L;
        BudgetGroupDescription description = new BudgetGroupDescription("Group with Members");

        BudgetGroupDomainEntity result = budgetGroupFactoryAdapter.createBudgetGroupFromRequest(userId, description);

        assertThat(result).isNotNull();
        assertThat(result.getListOfMembersId()).containsExactly(userId);
    }

    @Test
    void createBudgetGroupFromRequest_shouldHaveNoIdInitially() {
        Long userId = 1L;
        BudgetGroupDescription description = new BudgetGroupDescription("Group with No ID");

        BudgetGroupDomainEntity result = budgetGroupFactoryAdapter.createBudgetGroupFromRequest(userId, description);

        assertThat(result.getId()).isNull();
    }

    @Test
    void createBudgetGroupFromRequest_shouldReturnNonNullBudgetGroup_whenValidDataProvided() {
        Long userId = 2L;
        BudgetGroupDescription description = new BudgetGroupDescription("Non-null Budget Group");

        BudgetGroupDomainEntity result = budgetGroupFactoryAdapter.createBudgetGroupFromRequest(userId, description);

        assertThat(result).isNotNull();
    }

    @Test
    void createBudgetGroupFromRequest_shouldInitializeMembersWithSingleAdmin() {
        Long userId = 3L;
        BudgetGroupDescription description = new BudgetGroupDescription("Initial Members Group");

        BudgetGroupDomainEntity result = budgetGroupFactoryAdapter.createBudgetGroupFromRequest(userId, description);

        assertThat(result.getListOfMembersId()).hasSize(1);
        assertThat(result.getListOfMembersId()).contains(userId);
    }

    @Test
    void createBudgetGroupFromRequest_shouldReturnBudgetGroupWithGivenDescription() {
        Long userId = 1L;
        BudgetGroupDescription description = new BudgetGroupDescription("Description Group");

        BudgetGroupDomainEntity result = budgetGroupFactoryAdapter.createBudgetGroupFromRequest(userId, description);

        assertThat(result.getDescription()).isEqualTo("Description Group");
    }
}
