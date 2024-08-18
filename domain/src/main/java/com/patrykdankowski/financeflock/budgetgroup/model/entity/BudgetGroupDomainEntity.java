package com.patrykdankowski.financeflock.budgetgroup.model.entity;

import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@ToString
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

    private Set<Long> listOfCategoriesId = new HashSet<>();

    public void updateInfo(String description) {
        if (description != null && !description.isBlank()) {
            this.description = description;
        }
    }

    public void updateListOfMembers(Set<Long> listOfIds) {
        listOfIds.forEach(element -> {
            if (element > 0) {
                this.listOfMembersId.add(element);
            }
        });
    }
    public void updateListOfCategories(Set<Long> listOfIds) {
        listOfIds.forEach(element -> {
            if (element > 0) {
                this.listOfMembersId.add(element);
            }
        });
    }
    public void addUser(Long userId){
        if(userId > 0){
            listOfMembersId.add(userId);
        }
    }
    public void removeUser(Long userId) {
        if(userId > 0){
            listOfMembersId.remove(userId);
        }
    }

    public void addCategory(Long categoryId) {
        if (categoryId > 0) {
           listOfCategoriesId.add(categoryId);
        }
    }
    public void removeCategory(Long categoryId) {
        if (categoryId > 0) {
            this.listOfCategoriesId.remove(categoryId);
        }
    }


    private BudgetGroupDomainEntity(final Long id,
                                   final String description,
                                   final Long ownerId) {
        this.id = id;
        this.description = description;
        this.ownerId = ownerId;

    }


    public String getDescription() {
        return description;
    }

    public Set<Long> getListOfMembersId() {
        return listOfMembersId;
    }


    public Long getOwnerId() {
        return ownerId;
    }

    public Long getId() {
        return id;
    }

    public Set<Long> getListOfCategoriesId() {
        return listOfCategoriesId;
    }

}
