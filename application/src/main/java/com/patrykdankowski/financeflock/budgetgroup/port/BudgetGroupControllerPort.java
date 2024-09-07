package com.patrykdankowski.financeflock.budgetgroup.port;

import com.patrykdankowski.financeflock.budgetgroup.dto.BudgetGroupDto;
import com.patrykdankowski.financeflock.budgetgroup.dto.EmailDto;
import com.patrykdankowski.financeflock.user.dto.UserDtoProjections;
import com.patrykdankowski.financeflock.user.dto.UserLightDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface BudgetGroupControllerPort {

    String createBudgetGroup(BudgetGroupDto budgetGroupDto);

    void deleteBudgetGroup(Long id);

    String addUserToGroup(Long id, EmailDto emailDto);

    String removeUserFromGroup(Long id, EmailDto emailDto);

    List<UserDtoProjections> listOfExpansesInGroup();

    List<UserLightDto> listOfMembers(
            @PathVariable("id") Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection);
}
