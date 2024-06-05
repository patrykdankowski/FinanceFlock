package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.auth.AuthenticationServicePort;
import com.patrykdankowski.financeflock.user.UserDomainEntity;
import com.patrykdankowski.financeflock.user.UserDtoProjections;
import com.patrykdankowski.financeflock.user.UserDtoResponse;
import com.patrykdankowski.financeflock.user.UserQueryRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
class BudgetGroupQueryServiceImpl implements BudgetGroupQueryService {

    private final AuthenticationServicePort authenticationService;
    private final CommonDomainServicePort commonDomainService;
    private final UserQueryRepositoryPort userQueryRepository;
    private final BudgetGroupQueryRepositoryPort budgetGroupQueryRepository;

    public BudgetGroupQueryServiceImpl(final AuthenticationServicePort authenticationService,
                                       final CommonDomainServicePort commonDomainService,
                                       final UserQueryRepositoryPort userQueryRepository,
                                       final BudgetGroupQueryRepositoryPort budgetGroupQueryRepository) {
        this.authenticationService = authenticationService;
        this.commonDomainService = commonDomainService;
        this.userQueryRepository = userQueryRepository;
        this.budgetGroupQueryRepository = budgetGroupQueryRepository;
    }

    @Override
    public List<UserDtoResponse> listOfUsersInGroup() {

        UserDomainEntity userFromContext = authenticationService.getUserFromContext();

        Long budgetGroupDomainEntityId = commonDomainService.checkIfGroupExistsOld(userFromContext);

        return getListOfMembers(budgetGroupDomainEntityId);
    }

    private List<UserDtoResponse> getListOfMembers(final Long budgetGroupDomainEntityId) {
        BudgetGroupDomainEntity budgetGroupDomainEntity = budgetGroupQueryRepository.findBudgetGroupById(budgetGroupDomainEntityId)
                .orElseThrow(() -> new BudgetGroupNotFoundException(budgetGroupDomainEntityId));

        Set<Long> listOfMembersId = budgetGroupDomainEntity.getListOfMembersId();
        List<Long> mappedId = listOfMembersId.stream().toList();
        List<UserDomainEntity> listOfUsers = userQueryRepository.findAllByIdIn(mappedId);

        return listOfUsers.stream().map(
                        user -> {
                            return new UserDtoResponse(
                                    user.getName(),
                                    user.getEmail());
                        }
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
