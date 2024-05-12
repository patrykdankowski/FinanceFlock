package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.budgetgroup.dto.BudgetGroupRequest;
import com.patrykdankowski.financeflock.budgetgroup.dto.EmailDtoReadModel;
import com.patrykdankowski.financeflock.user.dto.UserDtoProjections;
import com.patrykdankowski.financeflock.user.dto.UserDtoResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface BudgetGroupControllerApi {

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER')")
    ResponseEntity<String> createBudgetGroup(@Valid @RequestBody BudgetGroupRequest budgetGroupRequest);

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('GROUP_ADMIN')")
    ResponseEntity<Void> deleteBudgetGroup();

    @PostMapping("/addUser")
    @PreAuthorize("hasAnyAuthority('GROUP_ADMIN')")
    ResponseEntity<String> addUserToGroup(@RequestBody EmailDtoReadModel emailDto);

    @PostMapping("/removeUser")
    @PreAuthorize("hasAnyAuthority('GROUP_ADMIN')")
    ResponseEntity<String> removeUserFromGroup(@RequestBody EmailDtoReadModel emailDto);

    @PreAuthorize("hasAnyAuthority('GROUP_MEMBER','GROUP_ADMIN','USER')")
    @GetMapping("/listOfMembers")
    ResponseEntity<List<UserDtoResponse>> getListOfMembersInBudgetGroup();

    @PreAuthorize("hasAnyAuthority('GROUP_MEMBER','GROUP_ADMIN','USER')")
    @GetMapping("/list")
    ResponseEntity<List<UserDtoProjections>> getList();
}
