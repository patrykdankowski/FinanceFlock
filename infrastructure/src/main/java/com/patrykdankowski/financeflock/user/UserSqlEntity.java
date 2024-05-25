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

//
//    public static UserSqlEntity fromDomainUser(UserDomainEntity source) {
//        Set<ExpenseSqlEntity> expenseSqlEntities = source.getExpenseList().stream().map(
//                expense -> fromDomainExpense(expense)
//        ).collect(Collectors.toSet());
//
//
//        var result = new UserSqlEntity();
//        result.id = source.getId();
//        result.password = source.getPassword();
//        result.email = source.getEmail();
//        result.name = source.getName();
//        result.lastLoggedInAt = source.getLastLoggedInAt();
//        result.createdAt = source.getCreatedAt();
//        result.role = source.getRole();
//        result.budgetGroup = source.getBudgetGroup() == null ? null : fromDomainGroup(source.getBudgetGroup());
//        result.expenseList = source.getExpenseList() == null ? null : expenseSqlEntities;
//        result.shareData = source.isShareData();
//        return result;
//
//    }
//
//    public static UserDomainEntity toDomainUser(UserSqlEntity source) {
//
//        Set<ExpenseDomainEntity> expenseDomainEntities = source.expenseList.stream().map(
//                expense -> toDomainExpense(expense)
//        ).collect(Collectors.toSet());
//
//
//        return UserDomainEntity.builder()
//                .id(source.id)
//                .budgetGroup(source.budgetGroup == null ? null : toDomainGroup(source.budgetGroup))
//                .password(source.password)
//                .email(source.email)
//                .lastLoggedInAt(source.lastLoggedInAt)
//                .createdAt(source.createdAt)
//                .name(source.name)
//                .role(source.role)
//                .expenseList(source.expenseList == null ? null : expenseDomainEntities)
//                .shareData(source.shareData)
//                .build();
//    }

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
    private Set<ExpenseSqlEntity> expenseList = new HashSet<>();

    private boolean shareData;

    public Set<ExpenseSqlEntity> getExpenseList() {
        return expenseList;
    }

    public void setExpenseList(final Set<ExpenseSqlEntity> expenseList) {
        this.expenseList = expenseList;
    }

    public BudgetGroupSqlEntity getBudgetGroup() {
        return budgetGroup;
    }

    public void setBudgetGroup(final BudgetGroupSqlEntity budgetGroup) {
        this.budgetGroup = budgetGroup;
    }
}
