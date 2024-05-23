package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.UserDtoProjections;
import com.patrykdankowski.financeflock.user.UserDtoResponse;

import java.util.List;

interface BudgetGroupControllerApi {

    String createBudgetGroup(BudgetGroupRequest budgetGroupRequest);

    void deleteBudgetGroup(Long id);

    String addUserToGroup(Long id, EmailDtoReadModel emailDto);

    String removeUserFromGroup(Long id, EmailDtoReadModel emailDto);

    List<UserDtoResponse> getListOfMembersInBudgetGroup();

    List<UserDtoProjections> getList();
}
