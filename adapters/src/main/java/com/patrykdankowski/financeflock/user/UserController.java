package com.patrykdankowski.financeflock.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
class UserController implements UserControllerApi {

    private final UserFacadeImpl userFacade;

    @Override
    public ResponseEntity<String> leaveBudgetGroup() {

        log.info("Attempting to leave budget group");

        userFacade.leaveBudgetGroup();

        log.info("Successfully left budget group");

        return ResponseEntity.status(HttpStatus.OK)
                .body("Successfully left group");
    }

    @Override
    public ResponseEntity<String> updateShareDataPreference() {

        log.info("Attempting to update share data preference");

        boolean isSharingData = userFacade.toggleShareData();

        log.info("Successfully updated share data preference");

        String message = isSharingData ? "You are now sharing your data" : "You are not sharing your data now";
        return ResponseEntity.status(HttpStatus.OK)
                .body(message);
    }


}
