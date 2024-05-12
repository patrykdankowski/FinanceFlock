package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.budgetgroup.dto.BudgetGroupRequest;
import com.patrykdankowski.financeflock.budgetgroup.dto.EmailDtoReadModel;
import com.patrykdankowski.financeflock.user.dto.UserDtoProjections;
import com.patrykdankowski.financeflock.user.dto.UserDtoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/budgetGroups")
@RequiredArgsConstructor
class BudgetGroupController {
    private final BudgetGroupFacade budgetGroupFacade;
    private final BudgetGroupQueryService budgetGroupQueryService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER')")
    ResponseEntity<String> createBudgetGroup(@Valid @RequestBody BudgetGroupRequest budgetGroupRequest) {
        budgetGroupFacade.createBudgetGroup(budgetGroupRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Budget group created");
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('GROUP_ADMIN')")
    ResponseEntity<Void> deleteBudgetGroup() {
        budgetGroupFacade.closeBudgetGroup();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/addUser")
//    @PreAuthorize("hasAnyAuthority('GROUP_ADMIN')")
    ResponseEntity<String> addUserToGroup(@RequestBody EmailDtoReadModel emailDto) {
        budgetGroupFacade.addUserToGroup(emailDto.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .body("User successfully added to group");
    }

    @PostMapping("/removeUser")
    @PreAuthorize("hasAnyAuthority('GROUP_ADMIN')")
    ResponseEntity<String> removeUserFromGroup(@RequestBody EmailDtoReadModel emailDto) {
        budgetGroupFacade.removeUserFromGroup(emailDto.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .body("User successfully removed from group");
    }

    @PreAuthorize("hasAnyAuthority('GROUP_MEMBER','GROUP_ADMIN','USER')")
    @GetMapping("/listOfMembers")
    ResponseEntity<List<UserDtoResponse>> getListOfMembersInBudgetGroup() {
        List<UserDtoResponse> list = budgetGroupQueryService.listOfUsersInGroup();
        return ResponseEntity.status(HttpStatus.OK)
                .body(list);
    }

    @PreAuthorize("hasAnyAuthority('GROUP_MEMBER','GROUP_ADMIN','USER')")
    @GetMapping("/list")
    ResponseEntity<List<UserDtoProjections>> getList() {

        List<UserDtoProjections> budgetGroupExpenses = budgetGroupQueryService.getBudgetGroupExpenses();
        return ResponseEntity.status(HttpStatus.OK)
                .body(budgetGroupExpenses);

    }


}
