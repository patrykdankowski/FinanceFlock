package com.patrykdankowski.financeflock.user;

import com.patrykdankowski.financeflock.user.dto.UserDtoProjections;
import org.springframework.data.repository.Repository;

import java.util.Set;

public interface UserQueryRepository extends Repository<User, Long> {
    //    @Query("SELECT u.name  FROM users u JOIN FETCH u.expenseList e WHERE u.shareData = true")
//@Query("SELECT u.name as name FROM users u JOIN u.expenseList e WHERE u.shareData = true")
//@Query("SELECT u.name FROM users u LEFT JOIN  u.expenseList e WHERE u.shareData = true")
    Set<UserDtoProjections> findAllByShareDataIsTrueAndBudgetGroup_Id(Long budgetGroupID);


    int count();

}
