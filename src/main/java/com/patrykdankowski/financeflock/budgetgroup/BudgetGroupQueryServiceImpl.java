package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.auth.AuthenticationService;
import com.patrykdankowski.financeflock.common.CommonDomainService;
import com.patrykdankowski.financeflock.exception.BudgetGroupNotFoundException;
import com.patrykdankowski.financeflock.user.UserQueryRepository;
import com.patrykdankowski.financeflock.user.dto.UserDtoProjections;
import com.patrykdankowski.financeflock.user.dto.UserDtoResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetGroupQueryServiceImpl implements BudgetGroupQueryService {

    private final AuthenticationService authenticationService;
    private final CommonDomainService commonDomainService;
    private final UserQueryRepository userQueryRepository;
    private final BudgetGroupQueryRepository budgetGroupQueryRepository;

    public BudgetGroupQueryServiceImpl(final AuthenticationService authenticationService,
                                       final CommonDomainService commonDomainService,
                                       final UserQueryRepository userQueryRepository, final BudgetGroupQueryRepository budgetGroupQueryRepository) {
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
