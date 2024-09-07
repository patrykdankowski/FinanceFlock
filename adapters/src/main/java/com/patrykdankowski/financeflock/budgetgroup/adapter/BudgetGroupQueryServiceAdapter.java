package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.auth.port.AuthenticationServicePort;
import com.patrykdankowski.financeflock.budgetgroup.exception.BudgetGroupNotFoundException;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupQueryRepositoryPort;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupQueryServicePort;
import com.patrykdankowski.financeflock.user.dto.UserLightDto;
import com.patrykdankowski.financeflock.user.dto.SimpleUserDomainEntity;
import com.patrykdankowski.financeflock.user.dto.UserDtoProjections;
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
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        SimpleUserDomainEntity loggedUser = authenticationService.getSimpleUserFromContext();

        final Long groupId = loggedUser.getBudgetGroupId();

        return getListOfMembers(groupId, pageable);
    }

    private List<UserLightDto> getListOfMembers(final Long budgetGroupId, final Pageable pageable) {
        log.info("Getting members of budget group {}", budgetGroupId);
        BudgetGroupDomainEntity budgetGroupDomainEntity = budgetGroupQueryRepository.findBudgetGroupById(budgetGroupId)
                .orElseThrow(() ->
                        new BudgetGroupNotFoundException(budgetGroupId));


        Long groupId = budgetGroupDomainEntity.getId();
//        Set<Long> listOfMembersId = budgetGroupDomainEntity.getListOfMembersId();
//        List<Long> mappedId = listOfMembersId.stream().toList();
log.info("before");
        List<UserLightDto> listOfUsers = userQueryRepository.findAllByBudgetGroup_Id(groupId, pageable);
        log.info("after");
//
//        return listOfUsers.stream().map(
//                        user -> new UserDtoResponse(
//                                user.getName(),
//                                user.getLastLoggedInAt())
//                )
//
//                .collect(Collectors.toList());

        return listOfUsers;
    }

    @Override
    public List<UserDtoProjections> getBudgetGroupExpenses() {
        return null;
//        // TODO logika zwiazana z podziałem na kategorie
//
//        UserDomainEntity userFromContext = authenticationService.getUserFromContext();
//
//        return new ArrayList<>(userQueryRepository.findAllByShareDataIsTrueAndBudgetGroup_Id(userFromContext.getBudgetGroupId()));
    }
}
