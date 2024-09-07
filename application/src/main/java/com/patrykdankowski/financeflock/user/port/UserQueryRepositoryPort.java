package com.patrykdankowski.financeflock.user.port;

import com.patrykdankowski.financeflock.user.dto.UserDetailsDto;
import com.patrykdankowski.financeflock.user.dto.UserDtoProjections;
import com.patrykdankowski.financeflock.user.dto.UserLightDto;
import com.patrykdankowski.financeflock.user.dto.SimpleUserDomainEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserQueryRepositoryPort {
    //    @Query("SELECT u.name  FROM users u JOIN FETCH u.expenseList e WHERE u.shareData = true")
//@Query("SELECT u.name as name FROM users u JOIN u.expenseList e WHERE u.shareData = true")
//@Query("SELECT u.name FROM users u LEFT JOIN  u.expenseList e WHERE u.shareData = true")
    Set<UserDtoProjections> findAllByShareDataIsTrueAndBudgetGroup_Id(Long budgetGroupID);


    int count();

//    @Query("SELECT u FROM User u WHERE u.id IN :ids")
//    List<UserDomainEntity> findAllById(Set<Long> ids);

    List<UserLightDto> findAllByBudgetGroup_Id(Long budgetGroupId, final Pageable pageable);

    Optional<SimpleUserDomainEntity> retrieveUserFromMail(String mail);

    Optional<UserDetailsDto> retrieveUserFromEmail(String email);

}
