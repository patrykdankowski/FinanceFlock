package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.UserDomainEntity;
import com.patrykdankowski.financeflock.user.UserSqlEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
@Table(name = "budget_groups")
@NoArgsConstructor
@AllArgsConstructor
public class BudgetGroupSqlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserSqlEntity owner;

    @OneToMany(mappedBy = "budgetGroup", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<UserSqlEntity> listOfMembers = new HashSet<>();

    public Long getId() {
        return id;
    }

    void setId(final Long id) {
        this.id = id;
    }

    String getDescription() {
        return description;
    }

    void setDescription(final String description) {
        this.description = description;
    }

    public UserSqlEntity getOwner() {
        return owner;
    }

    public void setOwner(final UserSqlEntity owner) {
        this.owner = owner;
    }

    public Set<UserSqlEntity> getListOfMembers() {
        return listOfMembers;
    }

    public void setListOfMembers(final Set<UserSqlEntity> listOfMembers) {
        this.listOfMembers = listOfMembers;
    }
}

