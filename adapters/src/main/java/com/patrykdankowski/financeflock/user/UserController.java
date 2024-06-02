package com.patrykdankowski.financeflock.user;

import jakarta.servlet.annotation.HttpConstraint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
class UserController implements UserControllerApi {

    private final UserFacadeImpl userFacade;

    @Override
    @PostMapping("/leaveGroup/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('GROUP_MEMBER')")
    public String leaveBudgetGroup(@PathVariable Long id) {

        log.info("Attempting to leave budget group");

        userFacade.leaveBudgetGroup(id);

        log.info("Successfully left budget group");

        return "Successfully left group";
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/updateShareDataPreference")
    @PreAuthorize("hasAnyAuthority('USER','GROUP_ADMIN','GROUP_MEMBER')")
    public String updateShareDataPreference() {

        log.info("Attempting to update share data preference");

        boolean isSharingData = userFacade.toggleShareData();

        log.info("Successfully updated share data preference");

        String message = isSharingData ? "You are now sharing your data" : "You are not sharing your data now";
        return message;
    }


}
