package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.UserDomainEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
public class BudgetGroupDomainEntity {


    private Long id;

    private String description;

    private Long ownerId;

    private Set<Long> listOfMembersId = new HashSet<>();


    //            BudgetGroupDomainEntity(final Builder builder) {
//        this.id = builder.id;
//        this.description = builder.description;
//        this.owner = builder.owner;
//        this.listOfMembers.add(builder.owner);
//    }
//    BudgetGroupDomainEntity(final Builder builder) {
//        this.id = builder.id;
//        this.description = builder.description;
//        this.ownerId = builder.ownerId;
//        this.listOfMembersId = builder.listOfMembersId;
//    }
//


    public String getDescription() {
        return description;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public BudgetGroupDomainEntity(final Long id, final String description, final Long ownerId, final Set<Long> listOfMembersId) {
        this.id = id;
        this.description = description;
        this.ownerId = ownerId;
        this.listOfMembersId = listOfMembersId;
    }

    public BudgetGroupDomainEntity() {
    }

    public Set<Long> getListOfMembersId() {
        return listOfMembersId;
    }

    public void setListOfMembersId(final Set<Long> listOfMembersId) {
        this.listOfMembersId = listOfMembersId;
    }

    public void setOwnerId(final Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

//    public static Builder builder() {
//        return new Builder();
//    }

    public Long getId() {
        return id;
    }

//    public static class Builder {
//        private Long id;
//
//        private String description;
//
//        private Long ownerId;
//
//        private Set<Long> listOfMembersId = new HashSet<>();
//
//        public Builder id(final Long id) {
//            this.id = id;
//            return this;
//        }
//
//        public Builder description(final String description) {
//            this.description = description;
//            return this;
//
//        }
//
//        public Builder owner(final Long ownerId) {
//            this.ownerId = ownerId;
//            return this;
//        }
//
//        public Builder member(final Long memberId) {
//            this.listOfMembersId.add(memberId);
//            return this;
//        }
//
//        public BudgetGroupDomainEntity build() {
//            return new BudgetGroupDomainEntity(this);
//        }
//
//
//    }


}
