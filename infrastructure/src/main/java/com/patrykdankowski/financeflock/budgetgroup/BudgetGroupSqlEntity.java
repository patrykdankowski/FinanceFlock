package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.UserDomainEntity;
import com.patrykdankowski.financeflock.user.UserSqlEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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

//    public static BudgetGroupSqlEntity fromDomainGroup(BudgetGroupDomainEntity budgetGroupDomainEntity) {
//        Set<UserSqlEntity> members = budgetGroupDomainEntity.getListOfMembers().stream().map(
//                member -> UserSqlEntity.fromDomainUser(member)
//        ).collect(Collectors.toSet());
//
//        var result = new BudgetGroupSqlEntity();
//        result.id = budgetGroupDomainEntity.getId();
//        result.description = budgetGroupDomainEntity.getDescription();
//        result.owner = fromDomainUser(budgetGroupDomainEntity.getOwner());
//        result.listOfMembers = members;
//        return result;
//    }
//
//    public static BudgetGroupDomainEntity toDomainGroup(BudgetGroupSqlEntity budgetGroupSqlEntity) {
//        Set<UserDomainEntity> members = budgetGroupSqlEntity.listOfMembers
//                .stream().map(
//                        member -> toDomainUser(member)
//                ).collect(Collectors.toSet());
//
//
//        var result = BudgetGroupDomainEntity.builder()
//                .id(budgetGroupSqlEntity.id)
//                .description(budgetGroupSqlEntity.description)
//                .owner(toDomainUser(budgetGroupSqlEntity.owner))
//                .members(members)
//                .build();
//        return result;
//    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserSqlEntity owner;

    @OneToMany(mappedBy = "budgetGroup", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<UserSqlEntity> listOfMembers = new HashSet<>();

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

