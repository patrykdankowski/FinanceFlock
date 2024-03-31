package com.patrykdankowski.financeflock.controller;

import com.patrykdankowski.financeflock.dto.SubUserDto;
import com.patrykdankowski.financeflock.dto.SubUserReadModel;
import com.patrykdankowski.financeflock.entity.Role;
import com.patrykdankowski.financeflock.entity.User;
import com.patrykdankowski.financeflock.repository.UserRepository;
import com.patrykdankowski.financeflock.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @PostMapping("/addSubUser")
    public ResponseEntity<String> addSubUser(@Valid @RequestBody SubUserDto subUserDto) {
        SubUserReadModel subUser = userService.createSubUser(subUserDto);
        String response = "Successfully registered a user with the given email" + subUser.getEmail();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/removeSubUser/{id}")
    public ResponseEntity<String> removeSubUserFromParentUser(@PathVariable Long id){
        userService.removeSubUser(id);
        String response = String.format("Successfully removed sub user with id %d from parent user",id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }


}
