package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.budgetgroup.dto.BudgetGroupDto;
import com.patrykdankowski.financeflock.user.User;
import com.patrykdankowski.financeflock.user.dto.UserDto;
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
import org.springframework.http.converter.json.GsonBuilderUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "budget_groups")
@NoArgsConstructor
public class BudgetGroup {

//    public static BudgetGroup fromDto(final BudgetGroupDto budgetGroupDto) {
//
//        Set<User> users = budgetGroupDto.getListOfMembers()
//                .stream().map(
//                        userDto -> User.fromDto(userDto)).collect(Collectors.toSet());
//
//        User owner = User.fromDto(budgetGroupDto.getOwner());
//
//        return BudgetGroup.builder()
//                .id(budgetGroupDto.getId())
//                .description(budgetGroupDto.getDescription())
//                .members(users)
//                .owner(owner)
//                .build();
//
//
//    }

    public BudgetGroupDto toDto() {


        UserDto ownerDto = this.owner.toDto();
        Set<UserDto> members = this.listOfMembers.stream().map(
                user -> user.toDto()
        ).collect(Collectors.toSet());


        return BudgetGroupDto.builder()
                .id(this.id)
                .description(this.description)
                .owner(ownerDto)
                .listOfMembers(members)
                .build();
    }


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

    public Long getId() {
        return id;
    }

    String getDescription() {
        return description;
    }

    User getOwner() {
        return owner;
    }

    Set<User> getListOfMembers() {
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

        public Builder member(final User member) {
            this.listOfMembers.add(member);
            return this;
        }

        public Builder members(final Set<User> members) {
            this.listOfMembers.addAll(members);
            return this;
        }

        public BudgetGroup build() {
            return new BudgetGroup(this);
        }


    }


}
