package com.patrykdankowski.financeflock.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.patrykdankowski.financeflock.budgetgroup.BudgetGroup;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.expense.Expense;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String password;

    private String email;

    private LocalDateTime lastLoggedInAt;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private BudgetGroup budgetGroup;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Expense> expenseList = new HashSet<>();

    private boolean shareData;

    private User(final Builder builder) {
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

    public void addExpense(Expense expense) {
        if (expenseList.contains(expense)) {
            return;
        }
        this.expenseList.add(expense);
        expense.setUser(this);
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

        private BudgetGroup budgetGroup;

        private Set<Expense> expenseList = new HashSet<>();

        private boolean shareData;


        public User build() {
            return new User(this);
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

        public Builder budgetGroup(final BudgetGroup budgetGroup) {
            this.budgetGroup = budgetGroup;
            return this;

        }

        public Builder expenseList(final Set<Expense> expenseList) {
            this.expenseList = expenseList;
            return this;

        }

        public Builder shareData(final boolean shareData) {
            this.shareData = shareData;
            return this;

        }
    }

}
