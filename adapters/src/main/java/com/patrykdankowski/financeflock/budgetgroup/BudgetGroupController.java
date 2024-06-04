package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.UserDtoProjections;
import com.patrykdankowski.financeflock.user.UserDtoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/budgetGroups")
@RequiredArgsConstructor
class BudgetGroupController implements BudgetGroupControllerApi {
    private final BudgetGroupFacade budgetGroupFacade;
    private final BudgetGroupQueryService budgetGroupQueryService;

    @Override
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public String createBudgetGroup(@Valid @RequestBody BudgetGroupRequest budgetGroupRequest) {
        Long budgetGroupId = budgetGroupFacade.createBudgetGroup(budgetGroupRequest);
        return String.format("Budget group created with id %d", budgetGroupId);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('GROUP_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBudgetGroup(@PathVariable Long id) {
        budgetGroupFacade.closeBudgetGroup(id);
    }

    @Override
    @PostMapping("/addUser/{id}")
    @PreAuthorize("hasAnyAuthority('GROUP_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public String addUserToGroup(@PathVariable Long id, @RequestBody EmailDtoReadModel emailDto) {
        budgetGroupFacade.addUserToGroup(emailDto.getEmail(), id);
        return "User successfully added to group";
    }

    @Override
    @PostMapping("/removeUser/{id}")
    @PreAuthorize("hasAnyAuthority('GROUP_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public String removeUserFromGroup(@PathVariable Long id, @RequestBody EmailDtoReadModel emailDto) {
        budgetGroupFacade.removeUserFromGroup(emailDto.getEmail(), id);
        return "User successfully removed from group";
    }

    @Override
    @GetMapping("/listOfMembers")
    @PreAuthorize("hasAnyAuthority('GROUP_MEMBER','GROUP_ADMIN','USER')")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDtoResponse> getListOfMembersInBudgetGroup() {
        List<UserDtoResponse> list = budgetGroupQueryService.listOfUsersInGroup();
        return list;
    }

    @Override
    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('GROUP_MEMBER','GROUP_ADMIN','USER')")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDtoProjections> getList() {

        List<UserDtoProjections> budgetGroupExpenses = budgetGroupQueryService.getBudgetGroupExpenses();
        return budgetGroupExpenses;

    }


}
