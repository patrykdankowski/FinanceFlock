package com.patrykdankowski.financeflock.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;

 interface UserControllerApi {
    @PreAuthorize("hasAnyAuthority('GROUP_MEMBER')")
    @PostMapping("/leaveGroup")
    ResponseEntity<String> leaveBudgetGroup();

    @PostMapping("/updateShareDataPreference")
    @PreAuthorize("hasAnyAuthority('USER','GROUP_ADMIN','GROUP_MEMBER')")
    ResponseEntity<String> updateShareDataPreference();
}
