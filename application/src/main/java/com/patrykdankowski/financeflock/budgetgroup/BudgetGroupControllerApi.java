package com.patrykdankowski.financeflock.budgetgroup;

import com.patrykdankowski.financeflock.user.UserDtoProjections;
import com.patrykdankowski.financeflock.user.UserDtoResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

 interface BudgetGroupControllerApi {

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER')")
    ResponseEntity<String> createBudgetGroup(@Valid @RequestBody BudgetGroupRequest budgetGroupRequest);

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('GROUP_ADMIN')")
    ResponseEntity<Void> deleteBudgetGroup(@PathVariable Long id);

    @PostMapping("/addUser/{id}")
    @PreAuthorize("hasAnyAuthority('GROUP_ADMIN')")
    ResponseEntity<String> addUserToGroup(@PathVariable Long id, @RequestBody EmailDtoReadModel emailDto);

    @PostMapping("/removeUser/{id}")
    @PreAuthorize("hasAnyAuthority('GROUP_ADMIN')")
    ResponseEntity<String> removeUserFromGroup(@PathVariable Long id,@RequestBody EmailDtoReadModel emailDto);

    @PreAuthorize("hasAnyAuthority('GROUP_MEMBER','GROUP_ADMIN','USER')")
    @GetMapping("/listOfMembers")
    ResponseEntity<List<UserDtoResponse>> getListOfMembersInBudgetGroup();

    @PreAuthorize("hasAnyAuthority('GROUP_MEMBER','GROUP_ADMIN','USER')")
    @GetMapping("/list")
    ResponseEntity<List<UserDtoProjections>> getList();
}
