package com.patrykdankowski.financeflock.controller;

import com.patrykdankowski.financeflock.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyAuthority('GROUP_MEMBER')")
    @PostMapping("/leaveGroup")
    public ResponseEntity<String> leaveBudgetGroup() {
        userService.leaveBudgetGroup();
        return ResponseEntity.status(HttpStatus.OK)
                .body("Successfully left group");
    }

    @PostMapping("/updateShareDataPreference")
    @PreAuthorize("hasAnyAuthority('USER','GROUP_ADMIN','GROUP_MEMBER')")
    public ResponseEntity<String> updateShareDataPreference() {
        boolean isSharingData = userService.toggleShareData();
        String message = isSharingData ? "You are now sharing your data" : "You are not sharing your data now";
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }


}
