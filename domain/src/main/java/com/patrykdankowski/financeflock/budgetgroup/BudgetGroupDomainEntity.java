package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.UserDomainEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BudgetGroupDomainEntity {


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
        listOfIds.forEach(element -> {
            if (element > 0) {
                this.listOfMembersId.add(element);
            }
        });
    }


    public BudgetGroupDomainEntity(final Long id,
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

}
