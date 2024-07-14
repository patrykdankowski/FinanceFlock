package com.patrykdankowski.financeflock.budgetgroup.port;

import com.patrykdankowski.financeflock.budgetgroup.dto.BudgetGroupRequest;
import com.patrykdankowski.financeflock.budgetgroup.dto.EmailRequest;
import com.patrykdankowski.financeflock.user.dto.UserDtoProjections;
import com.patrykdankowski.financeflock.user.model.record.UserDtoResponse;

import java.util.List;

public interface BudgetGroupControllerApi {

    String createBudgetGroup(BudgetGroupRequest budgetGroupRequest);

    void deleteBudgetGroup(Long id);

    String addUserToGroup(Long id, EmailRequest emailDto);

    String removeUserFromGroup(Long id, EmailRequest emailDto);

    List<UserDtoResponse> getListOfMembersInBudgetGroup();

    List<UserDtoProjections> getList();
}
