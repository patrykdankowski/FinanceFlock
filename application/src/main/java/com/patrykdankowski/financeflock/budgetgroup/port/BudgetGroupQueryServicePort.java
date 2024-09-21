package com.patrykdankowski.financeflock.budgetgroup.port;

import com.patrykdankowski.financeflock.user.dto.UserDto;
import com.patrykdankowski.financeflock.user.dto.UserLightDto;

import java.util.List;

public interface BudgetGroupQueryServicePort {

    List<UserLightDto> listOfUsersInGroup(final Long id, final int page, final int size, final String sortBy, final String sortDirection);

    List<UserDto> getBudgetGroupExpenses(final Long id, final int page, final int size, final String sortDirection);

    //    @Override
    //    public List<UserDto> getBudgetGroupExpenses(final Long id, final int page, final int size, final String sortDirection) {
    //        UserDomainEntity userFromContext = authenticationService.getUserFromContext();
    //
    //        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection),"totalExpenses");
    //        Pageable pageable = PageRequest.of(page, size, sort);
    //
    //
    //        final List<UserDto> userExpenseSummaries = userQueryRepository.findUserExpenseSummaries(userFromContext.getBudgetGroupId(), pageable);
    ////
    ////        userExpenseSummaries.forEach(userDto -> {
    ////            BigDecimal totalExpenses = userDto.getExpenses().stream()
    ////                    .map(ExpenseDto::getAmount)
    ////                    .reduce(BigDecimal.ZERO, BigDecimal::add);
    ////
    ////            userDto.setTotalExpensesForUser(totalExpenses);
    ////        });
    ////        if ("asc".equalsIgnoreCase(sortDirection)) {
    ////            userExpenseSummaries.sort(Comparator.comparing(UserDto::getTotalExpensesForUser));
    ////        } else if ("desc".equalsIgnoreCase(sortDirection)) {
    ////            userExpenseSummaries.sort(Comparator.comparing(UserDto::getTotalExpensesForUser).reversed());
    ////        }
    //
    //        return userExpenseSummaries;
    //    }
}
