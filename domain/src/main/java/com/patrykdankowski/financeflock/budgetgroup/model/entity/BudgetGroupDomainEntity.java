package com.patrykdankowski.financeflock.budgetgroup.model.entity;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class BudgetGroupDomainEntity {

    public static BudgetGroupDomainEntity buildBudgetGroup(final Long id,
                                                           final String description,
                                                           final Long ownerId) {

        return new BudgetGroupDomainEntity(id, description, ownerId);
    }


    private Long id;

    private Long ownerId;

    private String description;

    private Set<Long> listOfMembersId = new HashSet<>();


    public void updateInfo(String description) {
        if (description != null && !description.isBlank()) {
            this.description = description;
        }
    }

    public void updateListOfMembers(Set<Long> listOfIds) {
        if (listOfIds == null) {
            return;
        }
        listOfIds.forEach(element -> {
            if (element > 0) {
                this.listOfMembersId.add(element);
            }
        });
    }


    public void addUser(Long userId) {
        if (userId > 0 && !listOfMembersId.contains(userId)) {
            listOfMembersId.add(userId);
        }
    }

    public void removeUser(Long userId) {
        if (userId != null && userId > 0 && listOfMembersId.contains(userId)) {
            listOfMembersId.remove(userId);
        }
    }


    private BudgetGroupDomainEntity(final Long id,
                                    final String description,
                                    final Long ownerId) {
        this.id = id;
        this.description = description;
        this.ownerId = ownerId;

    }
}


