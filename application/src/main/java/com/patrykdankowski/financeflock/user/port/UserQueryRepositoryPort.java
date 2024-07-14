package com.patrykdankowski.financeflock.user.port;

import com.patrykdankowski.financeflock.user.dto.UserDtoProjections;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;

import java.util.List;
import java.util.Set;

public interface UserQueryRepositoryPort {
    //    @Query("SELECT u.name  FROM users u JOIN FETCH u.expenseList e WHERE u.shareData = true")
//@Query("SELECT u.name as name FROM users u JOIN u.expenseList e WHERE u.shareData = true")
//@Query("SELECT u.name FROM users u LEFT JOIN  u.expenseList e WHERE u.shareData = true")
    Set<UserDtoProjections> findAllByShareDataIsTrueAndBudgetGroup_Id(Long budgetGroupID);


    int count();

//    @Query("SELECT u FROM User u WHERE u.id IN :ids")
//    List<UserDomainEntity> findAllById(Set<Long> ids);

    List<UserDomainEntity> findAllByIdIn(List<Long> ids);

}
