package com.patrykdankowski.financeflock.budgetgroup.adapter;

import com.patrykdankowski.financeflock.budgetgroup.dto.BudgetGroupDto;
import com.patrykdankowski.financeflock.budgetgroup.dto.EmailDto;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupControllerPort;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupFacadePort;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupQueryServicePort;
import com.patrykdankowski.financeflock.user.dto.UserDto;
import com.patrykdankowski.financeflock.user.dto.UserLightDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/budgetGroups")
@RequiredArgsConstructor
class BudgetGroupControllerAdapter implements BudgetGroupControllerPort {
    private static final Logger log = LoggerFactory.getLogger(BudgetGroupControllerAdapter.class);
    private final BudgetGroupFacadePort budgetGroupFacade;
    private final BudgetGroupQueryServicePort budgetGroupQueryService;

    @Override
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public String createBudgetGroup(@Valid @RequestBody BudgetGroupDto budgetGroupDto) {
        Long budgetGroupId = budgetGroupFacade.createBudgetGroup(budgetGroupDto);
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
    public String addUserToGroup(@PathVariable Long id,
                                 @Valid @RequestBody EmailDto emailDto) {
        budgetGroupFacade.addUserToGroup(emailDto, id);
        return "User successfully added to group";
    }

    @Override
    @PostMapping("/removeUser/{id}")
    @PreAuthorize("hasAnyAuthority('GROUP_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public String removeUserFromGroup(@PathVariable Long id,
                                      @Valid @RequestBody EmailDto emailDto) {
        log.info("1");
        budgetGroupFacade.removeUserFromGroup(emailDto, id);
        return "User successfully removed from group";
    }

    @Override
    @GetMapping("/listOfMembers/{id}")
    @PreAuthorize("hasAnyAuthority('GROUP_MEMBER','GROUP_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public List<UserLightDto> listOfMembers(Long id,
                                            int page,
                                            int size,
                                            String sortBy,
                                            String sortDirection) {
        return budgetGroupQueryService.listOfUsersInGroup(id, page, size, sortBy, sortDirection);
    }

    @Override
    @GetMapping("/listOfExpanses/{id}")
    @PreAuthorize("hasAnyAuthority('GROUP_MEMBER','GROUP_ADMIN','USER')")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> listOfExpansesInGroup(Long id,
                                               int page,
                                               int size,
                                               String sortDirection) {

        return budgetGroupQueryService.getBudgetGroupExpenses(id, page, size,sortDirection);

    }


}
