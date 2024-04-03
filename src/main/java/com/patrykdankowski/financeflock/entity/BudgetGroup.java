package com.patrykdankowski.financeflock.entity;

import com.patrykdankowski.financeflock.dto.BudgetGroupDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "budget_groups")
@Getter
@Setter
public class BudgetGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "budgetGroup", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<User> listOfMembers = new HashSet<>();

    public static BudgetGroup create(User user, BudgetGroupDto budgetGroupDto) {
        BudgetGroup budgetGroup = new BudgetGroup();
        budgetGroup.setOwner(user);
        budgetGroup.getListOfMembers().add(user);
        budgetGroup.setDescription(budgetGroupDto.getDescription());
        return budgetGroup;

    }

}
