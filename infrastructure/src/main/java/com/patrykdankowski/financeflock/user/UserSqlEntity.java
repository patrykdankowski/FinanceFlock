package com.patrykdankowski.financeflock.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.patrykdankowski.financeflock.budgetgroup.BudgetGroupSqlEntity;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.expense.ExpenseSqlEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class UserSqlEntity {

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private BudgetGroupSqlEntity budgetGroup;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<ExpenseSqlEntity> expenseList = new HashSet<>();

    private boolean shareData;

    private LocalDateTime lastToggledShareData;

    public LocalDateTime getLastToggledShareData() {

        return lastToggledShareData;
    }

    public void setLastToggledShareData(LocalDateTime lastToggledShareData) {
        this.lastToggledShareData = lastToggledShareData;
    }

    public boolean isShareData() {
        return shareData;
    }

    public void setShareData(final boolean shareData) {
        this.shareData = shareData;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public LocalDateTime getLastLoggedInAt() {
        return lastLoggedInAt;
    }

    public void setLastLoggedInAt(final LocalDateTime lastLoggedInAt) {
        this.lastLoggedInAt = lastLoggedInAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(final Role role) {
        this.role = role;
    }

    public BudgetGroupSqlEntity getBudgetGroup() {
        return budgetGroup;
    }

    public void setBudgetGroup(final BudgetGroupSqlEntity budgetGroup) {
        this.budgetGroup = budgetGroup;
    }

    public Set<ExpenseSqlEntity> getExpenseList() {
        return expenseList;
    }

    public void setExpenseList(final Set<ExpenseSqlEntity> expenseList) {
        this.expenseList = expenseList;
    }

}
