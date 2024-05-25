package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.UserDomainEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class BudgetGroupDomainEntity {


    private Long id;

    private String description;

    private UserDomainEntity owner;

    private Set<UserDomainEntity> listOfMembers = new HashSet<>();

//            BudgetGroupDomainEntity(final Builder builder) {
//        this.id = builder.id;
//        this.description = builder.description;
//        this.owner = builder.owner;
//        this.listOfMembers.add(builder.owner);
//    }
    BudgetGroupDomainEntity(final Builder builder) {
        this.id = builder.id;
        this.description = builder.description;
        this.owner = builder.owner;
        this.listOfMembers = builder.listOfMembers;
    }


    public UserDomainEntity getOwner() {
        return owner;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public static class Builder {
        private Long id;

        private String description;

        private UserDomainEntity owner;

        private Set<UserDomainEntity> listOfMembers = new HashSet<>();

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder description(final String description) {
            this.description = description;
            return this;

        }
        public Builder listOfMembers(final UserDomainEntity owner) {
            this.owner = owner;
            return this;
        }
        public Builder owner(final UserDomainEntity owner) {
            this.owner = owner;
            return this;
        }

        public Builder member(final UserDomainEntity member) {
            this.listOfMembers.add(member);
            return this;
        }

        public Builder members(final Set<UserDomainEntity> members) {
            this.listOfMembers.addAll(members);
            return this;
        }

        public BudgetGroupDomainEntity build() {
            return new BudgetGroupDomainEntity(this);
        }


    }


}
