package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.auth.port.AuthenticationServicePort;
import com.patrykdankowski.financeflock.budgetgroup.exception.BudgetGroupNotFoundException;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupQueryRepositoryPort;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupQueryServicePort;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.dto.UserDtoProjections;
import com.patrykdankowski.financeflock.user.model.record.UserDtoResponse;
import com.patrykdankowski.financeflock.user.port.UserQueryRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
class BudgetGroupQueryServiceImpl implements BudgetGroupQueryServicePort {

    private final AuthenticationServicePort authenticationService;
    private final UserQueryRepositoryPort userQueryRepository;
    private final BudgetGroupQueryRepositoryPort budgetGroupQueryRepository;

    public BudgetGroupQueryServiceImpl(final AuthenticationServicePort authenticationService,
                                       final UserQueryRepositoryPort userQueryRepository,
                                       final BudgetGroupQueryRepositoryPort budgetGroupQueryRepository) {
        this.authenticationService = authenticationService;
        this.userQueryRepository = userQueryRepository;
        this.budgetGroupQueryRepository = budgetGroupQueryRepository;
    }

    @Override
    public List<UserDtoResponse> listOfUsersInGroup() {

//        UserDomainEntity userFromContext = authenticationService.getUserFromContext();
//
//        budgetGroupValidator.checkIfGroupIsNotNull(userFromContext);
//
//        return getListOfMembers(budgetGroupById);
        return null;
    }

    private List<UserDtoResponse> getListOfMembers(final Long budgetGroupDomainEntityId) {
        BudgetGroupDomainEntity budgetGroupDomainEntity = budgetGroupQueryRepository.findBudgetGroupById(budgetGroupDomainEntityId)
                .orElseThrow(() -> new BudgetGroupNotFoundException(budgetGroupDomainEntityId));

        Set<Long> listOfMembersId = budgetGroupDomainEntity.getListOfMembersId();
        List<Long> mappedId = listOfMembersId.stream().toList();
        List<UserDomainEntity> listOfUsers = userQueryRepository.findAllByIdIn(mappedId);

        return listOfUsers.stream().map(
                        user -> new UserDtoResponse(
                                user.getName(),
                                user.getEmail())
                )

                .collect(Collectors.toList());
    }

    @Override
    public List<UserDtoProjections> getBudgetGroupExpenses() {

        // TODO logika zwiazana z podziałem na kategorie

        UserDomainEntity userFromContext = authenticationService.getUserFromContext();

        return new ArrayList<>(userQueryRepository.findAllByShareDataIsTrueAndBudgetGroup_Id(userFromContext.getBudgetGroupId()));
    }
}
