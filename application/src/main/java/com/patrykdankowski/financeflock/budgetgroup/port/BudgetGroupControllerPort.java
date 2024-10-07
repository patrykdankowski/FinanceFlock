package com.patrykdankowski.financeflock.budgetgroup.port;

import com.patrykdankowski.financeflock.budgetgroup.dto.BudgetGroupDto;
import com.patrykdankowski.financeflock.budgetgroup.dto.EmailDto;
import com.patrykdankowski.financeflock.user.dto.UserDto;
import com.patrykdankowski.financeflock.user.dto.UserLightDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface BudgetGroupControllerPort {

    String createBudgetGroup(BudgetGroupDto budgetGroupDto);

    void deleteBudgetGroup(Long id);

    String addUserToGroup(Long id, EmailDto emailDto);

    String removeUserFromGroup(Long id, EmailDto emailDto);

    List<UserDto> listOfExpansesInGroup(
            Long id,
            int page,
            int size,
            String sortDirection);

    List<UserLightDto> listOfMembers(Long id,
                                     int page,
                                     int size,
                                     String sortBy,
                                     String sortDirection);
}