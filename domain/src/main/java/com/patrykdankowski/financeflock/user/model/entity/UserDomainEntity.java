package com.patrykdankowski.financeflock.user.model.entity;

import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.common.ShareDataPreferenceException;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@ToString
@AllArgsConstructor
@Getter
public class UserDomainEntity {

    private Long id;
    private String name;
    private String password;
    private String email;
    private LocalDateTime createdAt;

    private Role role;

    private Long budgetGroupId;

    private Set<Long> expenseListId = new HashSet<>();

    private LocalDateTime lastLoggedInAt;

    private boolean shareData;
    private LocalDateTime lastToggledShareData;


    public UserDomainEntity(Long id, String name, String password, String email, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.createdAt = createdAt;
    }

    public void login() {
        this.lastLoggedInAt = LocalDateTime.now();
    }

    public void updateInfo(boolean shareData,
                           LocalDateTime lastToggledShareData,
                           LocalDateTime lastLoggedInAt) {
        this.shareData = shareData;
        this.lastToggledShareData = lastToggledShareData;
        this.lastLoggedInAt = lastLoggedInAt;
    }

    public void changeRole(Role newRole) {
        if (newRole != null && newRole != this.role) {
            this.role = newRole;
        }
    }

    public void manageGroupMembership(Long budgetGroupId, Role role) {
        if (budgetGroupId != null) {
            this.budgetGroupId = budgetGroupId;
            this.role = role;
        } else {
            this.budgetGroupId = null;
            this.role = Role.USER;
        }
    }


    public void addExpense(Long expenseDomainId) {
        if (expenseListId.contains(expenseDomainId)) {
            return;
        }
        this.expenseListId.add(expenseDomainId);
    }

    public void removeExpense(Long expenseDomainId) {
        if (expenseDomainId != null) {
            this.expenseListId.remove(expenseDomainId);
        }
    }

    public void toggleShareData() {
        var now = LocalDateTime.now();
        if (canToggleShareData(now)) {
            this.shareData = !this.shareData;
            this.lastToggledShareData = now;
        }
    }

    public void initializeShareData() {
        if (this.id != null) {
            throw new IllegalStateException("Cannot initialize shareData for an existing user.");
        }
        this.shareData = true;
    }

    private boolean canToggleShareData(LocalDateTime now) {
        if (now == null) {
            throw new IllegalStateException("test");
        }
        if (this.lastToggledShareData != null) {
            Duration duration = Duration.between(this.lastToggledShareData, now);
            if (duration.toMinutes() < 5) {
                throw new ShareDataPreferenceException(this.lastToggledShareData);
            }
        }
        return true;
    }


    //    public LocalDateTime getLastToggledShareData() {
//
//        return lastToggledShareData;
//    }
//
//    public void setLastToggledShareData(LocalDateTime lastToggledShareData) {
//        this.lastToggledShareData = lastToggledShareData;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public LocalDateTime getLastLoggedInAt() {
//        return lastLoggedInAt;
//    }
//
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public Role getRole() {
//        return role;
//    }
//
//    public Long getBudgetGroupId() {
//        return budgetGroupId;
//    }
//
//    public Set<Long> getExpenseListId() {
//        return expenseListId;
//    }
//
//    public boolean isShareData() {
//        return shareData;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public void setLastLoggedInAt(LocalDateTime lastLoggedInAt) {
//        this.lastLoggedInAt = lastLoggedInAt;
//    }
//
//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public void setRole(Role role) {
//        this.role = role;
//    }
//
//    public void setBudgetGroupId(Long budgetGroupId) {
//        this.budgetGroupId = budgetGroupId;
//    }
//
//    public void setExpenseListId(Set<Long> expenseListId) {
//        this.expenseListId = expenseListId;
//    }
//
//    public void setShareData(boolean shareData) {
//        this.shareData = shareData;
//    }

}
