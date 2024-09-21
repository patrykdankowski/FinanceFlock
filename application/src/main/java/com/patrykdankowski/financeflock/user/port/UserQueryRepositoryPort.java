package com.patrykdankowski.financeflock.user.port;

import com.patrykdankowski.financeflock.user.dto.UserDetailsDto;
import com.patrykdankowski.financeflock.user.dto.UserDto;
import com.patrykdankowski.financeflock.user.dto.SimpleUserDomainEntity;
import com.patrykdankowski.financeflock.user.dto.UserLightDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserQueryRepositoryPort {

    List<UserLightDto> findAllByBudgetGroup_Id(Long budgetGroupId, final Pageable pageable);

    Optional<SimpleUserDomainEntity> retrieveUserFromMail(String mail);

    Optional<UserDetailsDto> retrieveUserFromEmail(String email);

    List<UserDto> findUserExpenseSummaries(final Long groupId, Pageable pageable);


}
