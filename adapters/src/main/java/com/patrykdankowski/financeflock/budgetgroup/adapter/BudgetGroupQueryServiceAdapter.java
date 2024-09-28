package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.auth.port.AuthenticationServicePort;
import com.patrykdankowski.financeflock.budgetgroup.exception.BudgetGroupNotFoundException;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupQueryRepositoryPort;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupQueryServicePort;
import com.patrykdankowski.financeflock.user.dto.UserDto;
import com.patrykdankowski.financeflock.user.dto.SimpleUserDomainEntity;
import com.patrykdankowski.financeflock.user.dto.UserLightDto;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.port.UserQueryRepositoryPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
class BudgetGroupQueryServiceAdapter implements BudgetGroupQueryServicePort {

    private final AuthenticationServicePort authenticationService;
    private final UserQueryRepositoryPort userQueryRepository;
    private final BudgetGroupQueryRepositoryPort budgetGroupQueryRepository;

    public BudgetGroupQueryServiceAdapter(final AuthenticationServicePort authenticationService,
                                          final UserQueryRepositoryPort userQueryRepository,
                                          final BudgetGroupQueryRepositoryPort budgetGroupQueryRepository) {
        this.authenticationService = authenticationService;
        this.userQueryRepository = userQueryRepository;
        this.budgetGroupQueryRepository = budgetGroupQueryRepository;
    }

    @Override
    public List<UserLightDto> listOfUsersInGroup(final Long id, final int page, final int size, final String sortBy, final String sortDirection) {
        log.info("Starting process of retrieving list of users in group with ID: {}", id);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        SimpleUserDomainEntity loggedUser = authenticationService.getSimpleUserFromContext();

        final Long groupId = loggedUser.getBudgetGroupId();

        final List<UserLightDto> members = getListOfMembers(groupId, pageable);

        log.info("Successfully retrieved {} members from budget group ID: {}", members.size(), groupId);

        return members;
    }


    private List<UserLightDto> getListOfMembers(final Long budgetGroupId, final Pageable pageable) {
        log.info("Getting members of budget group {}", budgetGroupId);
        BudgetGroupDomainEntity budgetGroupDomainEntity = budgetGroupQueryRepository.findBudgetGroupById(budgetGroupId)
                .orElseThrow(() ->
                        new BudgetGroupNotFoundException(budgetGroupId));


        Long groupId = budgetGroupDomainEntity.getId();

        return userQueryRepository.findAllByBudgetGroup_Id(groupId, pageable);


    }

    @Override
    public List<UserDto> getBudgetGroupExpenses(final Long id, final int page, final int size, final String sortDirection) {
        log.info("Starting process of retrieving expenses for budget group with ID: {}", id);

        UserDomainEntity userFromContext = authenticationService.getFullUserFromContext();

        Pageable pageable = getPageable(page, size, sortDirection);

        final List<UserDto> usersExpanses = userQueryRepository.findUserExpenseSummaries(userFromContext.getBudgetGroupId(), pageable);

        log.info("Successfully retrieved expenses for group ID: {} with size: {}", userFromContext.getBudgetGroupId(), usersExpanses.size());

        return usersExpanses;
    }

    private Pageable getPageable(final int page, final int size, final String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "totalExpensesForUser");
        return PageRequest.of(page, size, sort);
    }
}