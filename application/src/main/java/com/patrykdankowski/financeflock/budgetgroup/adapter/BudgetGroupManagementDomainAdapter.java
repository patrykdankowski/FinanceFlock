package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupFactoryPort;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.model.record.BudgetGroupDescription;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupManagementDomainPort;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
class BudgetGroupManagementDomainAdapter implements BudgetGroupManagementDomainPort {


    private final BudgetGroupFactoryPort budgetGroupFactory;


    BudgetGroupManagementDomainAdapter(final BudgetGroupFactoryPort budgetGroupFactory) {
        this.budgetGroupFactory = budgetGroupFactory;

    }

    @Override
    public BudgetGroupDomainEntity createBudgetGroup(final BudgetGroupDescription budgetGroupDescription,
                                                     final Long userId) {

        return budgetGroupFactory.createBudgetGroupFromRequest(userId, budgetGroupDescription);


    }

    @Override
    public List<UserDomainEntity> closeBudgetGroup(final List<UserDomainEntity> listOfUsers) {
        //TODO zaimplementować metode informujaca all userów z grupy ze grupa została zamknięta

        List<UserDomainEntity> mapppedEntities = resetUsersRolesAndDetachFromGroup(listOfUsers);

        return mapppedEntities;

    }


    private List<UserDomainEntity> resetUsersRolesAndDetachFromGroup(final List<UserDomainEntity> listOfUsers) {
        return listOfUsers.stream().map(
                userToMap ->
                {
                    userToMap.manageGroupMembership(null, Role.USER);
                    return userToMap;
                }

        ).collect(Collectors.toList());
    }


}
