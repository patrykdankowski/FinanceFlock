package com.patrykdankowski.financeflock.user;

import com.patrykdankowski.financeflock.budgetgroup.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.expense.ExpenseDomainEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDomainEntity {

    private Long id;

    private String name;

    private String password;

    private String email;

    private LocalDateTime lastLoggedInAt;

    private LocalDateTime createdAt;

    private Role role;

    private BudgetGroupDomainEntity budgetGroup;

    private Set<ExpenseDomainEntity> expenseList = new HashSet<>();

    private boolean shareData;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getLastLoggedInAt() {
        return lastLoggedInAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Role getRole() {
        return role;
    }

    public BudgetGroupDomainEntity getBudgetGroup() {
        return budgetGroup;
    }

    public Set<ExpenseDomainEntity> getExpenseList() {
        return expenseList;
    }

    public boolean isShareData() {
        return shareData;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLastLoggedInAt(LocalDateTime lastLoggedInAt) {
        this.lastLoggedInAt = lastLoggedInAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setBudgetGroup(BudgetGroupDomainEntity budgetGroupDomainEntity) {
        this.budgetGroup = budgetGroupDomainEntity;
    }

    public void setExpenseList(Set<ExpenseDomainEntity> expenseList) {
        this.expenseList = expenseList;
    }

    public void setShareData(boolean shareData) {
        this.shareData = shareData;
    }

    private UserDomainEntity(final Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.password = builder.password;
        this.email = builder.email;
        this.lastLoggedInAt = builder.lastLoggedInAt;
        this.createdAt = builder.createdAt;
        this.role = builder.role;
        this.budgetGroup = builder.budgetGroup;
        this.expenseList = builder.expenseList;
        this.shareData = builder.shareData;
    }

    public Builder toBuilder() {
        return builder()
                .id(this.id)
                .name(this.name)
                .password(this.password)
                .email(this.email)
                .lastLoggedInAt(this.lastLoggedInAt)
                .createdAt(this.createdAt)
                .role(this.role)
                .budgetGroup(this.budgetGroup)
                .expenseList(this.expenseList)
                .shareData(this.shareData);
    }

    public static Builder builder() {
        return new Builder();
    }

    public void addExpense(ExpenseDomainEntity expenseDomainEntity) {
        if (expenseList.contains(expenseDomainEntity)) {
            return;
        }
        this.expenseList.add(expenseDomainEntity);
        expenseDomainEntity.setUser(this);
    }


    public static class Builder {

        private Builder() {
        }

        private Long id;

        private String name;

        private String password;

        private String email;

        private LocalDateTime lastLoggedInAt;

        private LocalDateTime createdAt;

        private Role role;

        private BudgetGroupDomainEntity budgetGroup;

        private Set<ExpenseDomainEntity> expenseList = new HashSet<>();

        private boolean shareData;


        public UserDomainEntity build() {
            return new UserDomainEntity(this);
        }


        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder name(final String name) {
            this.name = name;
            return this;

        }

        public Builder password(final String password) {
            this.password = password;
            return this;

        }

        public Builder email(final String email) {
            this.email = email;
            return this;

        }

        public Builder lastLoggedInAt(final LocalDateTime lastLoggedInAt) {
            this.lastLoggedInAt = lastLoggedInAt;
            return this;

        }

        public Builder createdAt(final LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;

        }

        public Builder role(final Role role) {
            this.role = role;
            return this;

        }

        public Builder budgetGroup(final BudgetGroupDomainEntity budgetGroup) {
            this.budgetGroup = budgetGroup;
            return this;

        }

        public Builder expenseList(final Set<ExpenseDomainEntity> expenseList) {
            this.expenseList = expenseList;
            return this;

        }

        public Builder shareData(final boolean shareData) {
            this.shareData = shareData;
            return this;

        }
    }

}
