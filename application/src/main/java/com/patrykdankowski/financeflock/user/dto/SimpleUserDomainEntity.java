package com.patrykdankowski.financeflock.user.dto;

import com.patrykdankowski.financeflock.common.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@ToString
@AllArgsConstructor
@Getter
public class SimpleUserDomainEntity {

    public static SimpleUserDomainEntity buildUser(Long id,
                                                   String name,
                                                   String email,
                                                   Long budgetGroupId,
                                                   Role role,
                                                   LocalDateTime lastLoggedInAt,
                                                   boolean shareData,
                                                   LocalDateTime lastToggledShareData,
                                                   LocalDateTime createdAt) {

        return new SimpleUserDomainEntity(id,
                name,
                email,
                budgetGroupId,
                role,
                lastLoggedInAt,
                shareData,
                lastToggledShareData,
                createdAt);

    }

    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private Long budgetGroupId;
    private Role role;
    private LocalDateTime lastLoggedInAt;
    private boolean shareData;
    private LocalDateTime lastToggledShareData;


    private SimpleUserDomainEntity(Long id, String name, String email, Long budgetGroupId, Role role, LocalDateTime lastLoggedInAt, boolean shareData, LocalDateTime lastToggledShareData, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.budgetGroupId = budgetGroupId;
        this.role = role;
        this.lastLoggedInAt = lastLoggedInAt;
        this.shareData = shareData;
        this.lastToggledShareData = lastToggledShareData;
    }

}
