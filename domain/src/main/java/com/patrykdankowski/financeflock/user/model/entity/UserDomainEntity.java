package com.patrykdankowski.financeflock.user.model.entity;

import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.exception.ToEarlyShareDataPreferenceException;
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

    public static UserDomainEntity buildUser(Long id, String name, String password, String email, LocalDateTime createdAt) {

        return new UserDomainEntity(id, name, password, email, createdAt);

    }

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


    private UserDomainEntity(Long id, String name, String password, String email, LocalDateTime createdAt) {
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
        if (budgetGroupId == null) {
            this.budgetGroupId = null;
            this.role = Role.USER;
        } else if (budgetGroupId > 0) {
            this.budgetGroupId = budgetGroupId;
            this.role = role;
        } else {
            throw new IllegalStateException("Given id group is less than 0");
        }
    }
//    public void manageGroupMembership(Long budgetGroupId, Role role) {
//        if (budgetGroupId > 0) {
//            this.budgetGroupId = budgetGroupId;
//            this.role = role;
//        } else if (budgetGroupId == null) {
//            this.budgetGroupId = null;
//            this.role = Role.USER;
//        } else {
//            throw new IllegalStateException("Given id group is less than 0");
//        }
//    }


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
            this.lastToggledShareData = LocalDateTime.now();
        }
    }

    private boolean canToggleShareData(LocalDateTime now) {
        if (this.lastToggledShareData != null) {
            Duration duration = Duration.between(this.lastToggledShareData, now);
            if (duration.toMinutes() < 5) {
                throw new ToEarlyShareDataPreferenceException(this.lastToggledShareData);
            }
        }
        return true;
    }

    public void initializeShareData() {
        if (this.id != null) {
            throw new IllegalStateException("Cannot initialize shareData for an existing user.");
        }
        this.shareData = true;
    }
}
