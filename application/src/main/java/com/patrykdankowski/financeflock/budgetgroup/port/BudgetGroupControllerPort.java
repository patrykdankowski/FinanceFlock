package com.patrykdankowski.financeflock.budgetgroup.port;

import com.patrykdankowski.financeflock.budgetgroup.dto.BudgetGroupDto;
import com.patrykdankowski.financeflock.budgetgroup.dto.EmailDto;
import com.patrykdankowski.financeflock.user.dto.UserDtoProjections;
import com.patrykdankowski.financeflock.user.model.record.UserDtoResponse;

import java.util.List;

public interface BudgetGroupControllerPort {

    String createBudgetGroup(BudgetGroupDto budgetGroupDto);

    void deleteBudgetGroup(Long id);

    String addUserToGroup(Long id, EmailDto emailDto);

    String removeUserFromGroup(Long id, EmailDto emailDto);

    List<UserDtoResponse> getListOfMembersInBudgetGroup();

    List<UserDtoProjections> getList();
}
