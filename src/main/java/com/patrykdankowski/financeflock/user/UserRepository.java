package com.patrykdankowski.financeflock.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsUserByEmail(String email);

//    @Query("SELECT u.name  FROM users u JOIN FETCH u.expenseList e WHERE u.shareData = true")
//@Query("SELECT u.name as name FROM users u JOIN u.expenseList e WHERE u.shareData = true")
//@Query("SELECT u.name FROM users u LEFT JOIN  u.expenseList e WHERE u.shareData = true")

    Set<UserDtoProjections> findAllByShareDataIsTrueAndBudgetGroup_Id(Long budgetGroupID);


}
