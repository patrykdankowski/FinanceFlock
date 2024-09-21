package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.auth.port.AuthenticationServicePort;
import com.patrykdankowski.financeflock.budgetgroup.exception.BudgetGroupNotFoundException;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupQueryRepositoryPort;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupQueryServicePort;
import com.patrykdankowski.financeflock.expense.port.ExpenseQueryRepositoryPort;
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

        return userQueryRepository.findAllByBudgetGroup_Id(groupId, pageable);


    }

    //        @Override
//    public List<UserDto> getBudgetGroupExpenses(final Long id, final int page, final int size, final String sortDirection) {
//        UserDomainEntity userFromContext = authenticationService.getUserFromContext();
//
//        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection),"totalExpenses");
//        Pageable pageable = PageRequest.of(page, size, sort);
//
//
//        final List<UserDto> userExpenseSummaries = userQueryRepository.findUserExpenseSummaries(userFromContext.getBudgetGroupId());
//
//        userExpenseSummaries.forEach(userDto -> {
//            BigDecimal totalExpenses = userDto.getExpenses().stream()
//                    .map(ExpenseDto::getAmount)
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//            userDto.setTotalExpensesForUser(totalExpenses);
//        });
//        if ("asc".equalsIgnoreCase(sortDirection)) {
//            userExpenseSummaries.sort(Comparator.comparing(UserDto::getTotalExpensesForUser));
//        } else if ("desc".equalsIgnoreCase(sortDirection)) {
//            userExpenseSummaries.sort(Comparator.comparing(UserDto::getTotalExpensesForUser).reversed());
//        }
//
//        return userExpenseSummaries;
//    }
//@Override
//public List<UserDto> getBudgetGroupExpenses(final Long id, final int page, final int size, final String sortDirection) {
//    UserDomainEntity userFromContext = authenticationService.getUserFromContext();
//
//    Sort sort = Sort.by(Sort.Direction.fromString(sortDirection),"totalExpenses");
//    Pageable pageable = PageRequest.of(page, size, sort);
//
//
//    final List<UserDto> userExpenseSummaries = userQueryRepository.findUserExpenseSummaries(userFromContext.getBudgetGroupId(), pageable);
//
//    userExpenseSummaries.forEach(userDto -> {
//        BigDecimal totalExpenses = userDto.getExpenses().stream()
//                .map(ExpenseDto::getAmount)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        userDto.setTotalExpensesForUser(totalExpenses);
//    });
//    if ("asc".equalsIgnoreCase(sortDirection)) {
//        userExpenseSummaries.sort(Comparator.comparing(UserDto::getTotalExpensesForUser));
//    } else if ("desc".equalsIgnoreCase(sortDirection)) {
//        userExpenseSummaries.sort(Comparator.comparing(UserDto::getTotalExpensesForUser).reversed());
//    }
//
//    return userExpenseSummaries;
//}
    @Override
    public List<UserDto> getBudgetGroupExpenses(final Long id, final int page, final int size, final String sortDirection) {
//        UserDomainEntity userFromContext = authenticationService.getUserFromContext();
//        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "totalExpensesForUser");
//        Pageable pageable = PageRequest.of(page, size, sort);
//        // Ustawienie paginacji
////        Pageable pageable = PageRequest.of(page, size);
//
//        // Wywołanie metody repozytorium, która zwraca mapowane DTO
//        return userQueryRepository.findUserExpenseSummaries(userFromContext.getBudgetGroupId(), pageable);
        UserDomainEntity userFromContext = authenticationService.getUserFromContext();

        // Tworzymy Pageable z sortowaniem i paginacją
        Pageable pageable = getPageable(page, size, sortDirection);

        // Wywołujemy repozytorium, które zwraca już zmapowane DTO
        return userQueryRepository.findUserExpenseSummaries(userFromContext.getBudgetGroupId(), pageable);
    }

    private Pageable getPageable(final int page, final int size, final String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "totalExpensesForUser");
        return PageRequest.of(page, size, sort);
    }
}