package com.patrykdankowski.financeflock.controller;

import com.patrykdankowski.financeflock.entity.User;
import com.patrykdankowski.financeflock.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/test")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        user.setCreatedAt(LocalDateTime.now());

        User userSaved = userRepository.save(user);
        return ResponseEntity.ok(userSaved);
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/test/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);

        return ResponseEntity.of(user);

    }


}
