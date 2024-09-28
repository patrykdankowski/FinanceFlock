package com.patrykdankowski.financeflock.budgetgroup.entity;

import com.patrykdankowski.financeflock.user.entity.UserSqlEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


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

    @OneToMany(mappedBy = "budgetGroup", fetch = FetchType.LAZY)
    private Set<UserSqlEntity> listOfMembers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
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

