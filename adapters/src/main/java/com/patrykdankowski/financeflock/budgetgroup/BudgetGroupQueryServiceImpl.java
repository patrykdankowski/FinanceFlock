package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.auth.AuthenticationServicePort;
import com.patrykdankowski.financeflock.user.UserDtoProjections;
import com.patrykdankowski.financeflock.user.UserDtoResponse;
import com.patrykdankowski.financeflock.user.UserQueryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
 class BudgetGroupQueryServiceImpl implements BudgetGroupQueryService {

    private final AuthenticationServicePort authenticationService;
    private final CommonDomainServicePort commonDomainService;
    private final UserQueryRepository userQueryRepository;
    private final BudgetGroupQueryRepository budgetGroupQueryRepository;

    public BudgetGroupQueryServiceImpl(final AuthenticationServicePort authenticationService,
                                       final CommonDomainServicePort commonDomainService,
                                       final UserQueryRepository userQueryRepository,
                                       final BudgetGroupQueryRepository budgetGroupQueryRepository) {
        this.authenticationService = authenticationService;
        this.commonDomainService = commonDomainService;
        this.userQueryRepository = userQueryRepository;
        this.budgetGroupQueryRepository = budgetGroupQueryRepository;
    }

    @Override
    public List<UserDtoResponse> listOfUsersInGroup() {

        var userFromContext = authenticationService.getUserFromContext();

        BudgetGroup budgetGroup = commonDomainService.validateAndGetUserGroup(userFromContext);

        return getListOfMembers(budgetGroup);
    }

    private List<UserDtoResponse> getListOfMembers(final BudgetGroup budgetGroup) {
        return budgetGroupQueryRepository.findBudgetGroupById(budgetGroup.getId()).map(
                        group -> group.getListOfMembers().stream().map(
                                user -> new UserDtoResponse(user.getName(), user.getEmail())
                        ).collect(Collectors.toList()))
                .orElseThrow(() -> new BudgetGroupNotFoundException(budgetGroup.getId()));
    }

    @Override
    public List<UserDtoProjections> getBudgetGroupExpenses() {

        // TODO logika zwiazana z podziałem na kategorie

        var userFromContext = authenticationService.getUserFromContext();

        return new ArrayList<>(userQueryRepository.findAllByShareDataIsTrueAndBudgetGroup_Id(userFromContext.getBudgetGroup().getId()));
    }
}
