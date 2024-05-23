package com.patrykdankowski.financeflock.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.patrykdankowski.financeflock.budgetgroup.BudgetGroupSqlEntity;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.expense.ExpenseDomainEntity;
import com.patrykdankowski.financeflock.expense.ExpenseSqlEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.patrykdankowski.financeflock.budgetgroup.BudgetGroupSqlEntity.fromDomainGroup;
import static com.patrykdankowski.financeflock.budgetgroup.BudgetGroupSqlEntity.toDomainGroup;
import static com.patrykdankowski.financeflock.expense.ExpenseSqlEntity.fromDomainExpense;
import static com.patrykdankowski.financeflock.expense.ExpenseSqlEntity.toDomainExpense;

@Entity
@Table(name = "users")
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
public class UserSqlEntity {


    public static UserSqlEntity fromDomainUser(UserDomainEntity source) {
        Set<ExpenseSqlEntity> expenseSqlEntities = source.getExpenseList().stream().map(
                expense -> fromDomainExpense(expense)
        ).collect(Collectors.toSet());


        var result = new UserSqlEntity();
        result.id = source.getId();
        result.password = source.getPassword();
        result.email = source.getEmail();
        result.name=source.getName();
        result.lastLoggedInAt = source.getLastLoggedInAt();
        result.createdAt = source.getCreatedAt();
        result.role = source.getRole();
        result.budgetGroup = source.getBudgetGroup() == null ? null : fromDomainGroup(source.getBudgetGroup());
        result.expenseDomainEntityList = source.getExpenseList() == null ? null : expenseSqlEntities;
        result.shareData = source.isShareData();
        return result;

    }

    public static UserDomainEntity toDomainUser(UserSqlEntity source) {

        Set<ExpenseDomainEntity> expenseDomainEntities = source.expenseDomainEntityList.stream().map(
                expense -> toDomainExpense(expense)
        ).collect(Collectors.toSet());


        return UserDomainEntity.builder()
                .id(source.id)
                .budgetGroup(source.budgetGroup == null ? null : toDomainGroup(source.budgetGroup))
                .password(source.password)
                .email(source.email)
                .lastLoggedInAt(source.lastLoggedInAt)
                .createdAt(source.createdAt)
                .name(source.name)
                .role(source.role)
                .expenseList(source.expenseDomainEntityList == null ? null : expenseDomainEntities)
                .shareData(source.shareData)
                .build();
    }

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
    private BudgetGroupSqlEntity budgetGroup;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<ExpenseSqlEntity> expenseDomainEntityList = new HashSet<>();

    private boolean shareData;

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

    public void setBudgetGroup(BudgetGroupSqlEntity budgetGroupDomainEntity) {
        this.budgetGroup = budgetGroupDomainEntity;
    }

    public void setExpenseList(Set<ExpenseSqlEntity> expenseDomainEntityList) {
        this.expenseDomainEntityList = expenseDomainEntityList;
    }

    public void setShareData(boolean shareData) {
        this.shareData = shareData;
    }
}
