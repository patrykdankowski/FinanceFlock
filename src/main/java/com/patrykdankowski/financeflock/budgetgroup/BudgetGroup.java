package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "budget_groups")
@NoArgsConstructor
public class BudgetGroup {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String description;

        @ManyToOne
        @JoinColumn(name = "owner_id")
        private User owner;

        @OneToMany(mappedBy = "budgetGroup", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
        private final Set<User> listOfMembers = new HashSet<>();

        BudgetGroup(final Builder builder) {
            this.id = builder.id;
            this.description = builder.description;
            this.owner = builder.owner;
            this.listOfMembers.add(builder.owner);
        }

        public static Builder builder() {
            return new Builder();
        }

        Long getId() {
            return id;
        }

        User getOwner() {
            return owner;
        }

        public Set<User> getListOfMembers() {
            return listOfMembers;
        }

        public static class Builder {
            private Long id;

            private String description;

            private User owner;

            private Set<User> listOfMembers = new HashSet<>();

            public Builder id(final Long id) {
                this.id = id;
                return this;
            }

            public Builder description(final String description) {
                this.description = description;
                return this;

            }

            public Builder owner(final User owner) {
                this.owner = owner;
                return this;
            }

            public Builder listOfMembers(final User member) {
                this.listOfMembers.add(member);
                return this;
            }
//        public Builder members(final Set<User> members) {
//            this.listOfMembers.addAll(listOfMembers);
//            return this;
//        }

            public BudgetGroup build() {
                return new BudgetGroup(this);
            }


        }


    }
