package com.patrykdankowski.financeflock.controller;

import com.patrykdankowski.financeflock.dto.BudgetGroupDto;
import com.patrykdankowski.financeflock.dto.EmailDto;
import com.patrykdankowski.financeflock.dto.UserDto;
import com.patrykdankowski.financeflock.dto.projections.UserDtoProjections;
import com.patrykdankowski.financeflock.service.BudgetGroupService;
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
public class BudgetGroupController {
    private final BudgetGroupService budgetGroupService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<String> createBudgetGroup(@Valid @RequestBody BudgetGroupDto budgetGroupDto) {
        budgetGroupService.createBudgetGroup(budgetGroupDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Budget group created");
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('GROUP_ADMIN')")
    public ResponseEntity<Void> deleteBudgetGroup() {
        budgetGroupService.closeBudgetGroup();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/addUser")
    @PreAuthorize("hasAnyAuthority('GROUP_ADMIN')")
    public ResponseEntity<String> addUserToGroup(@RequestBody EmailDto emailDto) {
        budgetGroupService.addUserToGroup(emailDto.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .body("User successfully added to group");
    }

    @PostMapping("/removeUser")
    @PreAuthorize("hasAnyAuthority('GROUP_ADMIN')")
    public ResponseEntity<String> removeUserFromGroup(@RequestBody EmailDto emailDto) {
        budgetGroupService.removeUserFromGroup(emailDto.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .body("User successfully removed from group");
    }

    @PreAuthorize("hasAnyAuthority('GROUP_MEMBER','GROUP_ADMIN','USER')")
    @GetMapping("/listOfMembers")
    public ResponseEntity<List<UserDto>> getListOfMembersInBudgetGroup() {
        List<UserDto> list = budgetGroupService.listOfUsersInGroup();
        return ResponseEntity.status(HttpStatus.OK)
                .body(list);
    }

    @PreAuthorize("hasAnyAuthority('GROUP_MEMBER','GROUP_ADMIN','USER')")
    @GetMapping("/list")
    public ResponseEntity<List<UserDtoProjections>> getList() {

        List<UserDtoProjections> budgetGroupExpenses = budgetGroupService.getBudgetGroupExpenses();
        return ResponseEntity.status(HttpStatus.OK)
                .body(budgetGroupExpenses);

    }


}
