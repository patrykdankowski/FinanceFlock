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

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "budget_groups")
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

    //TODO przenieś do fabryki
    public static BudgetGroup create(User user, BudgetGroupDto budgetGroupDto) {
        BudgetGroup budgetGroup = new BudgetGroup();
        budgetGroup.setOwner(user);
        budgetGroup.getListOfMembers().add(user);
        budgetGroup.setDescription(budgetGroupDto.getDescription());
        return budgetGroup;

    }

     Long getId() {
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

     User getOwner() {
        return owner;
    }

     void setOwner(final User owner) {
        this.owner = owner;
    }

     Set<User> getListOfMembers() {
        return listOfMembers;
    }
}
