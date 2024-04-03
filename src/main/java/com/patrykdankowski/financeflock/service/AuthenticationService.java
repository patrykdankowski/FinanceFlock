package com.patrykdankowski.financeflock.service;

import com.patrykdankowski.financeflock.dto.LoginDto;
import com.patrykdankowski.financeflock.dto.RegisterDto;
import com.patrykdankowski.financeflock.entity.Role;
import com.patrykdankowski.financeflock.entity.User;
import com.patrykdankowski.financeflock.exception.EmailAlreadyExistsException;
import com.patrykdankowski.financeflock.exception.UserNotFoundException;
import com.patrykdankowski.financeflock.repository.UserRepository;
import com.patrykdankowski.financeflock.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        updateLastLoginAsync(authentication);

        return jwtTokenProvider.generateJwtToken(authentication);
    }

    public String register(RegisterDto registerDto) {
        if (userRepository.existsUserByEmail(registerDto.getEmail())) {
            throw new EmailAlreadyExistsException(registerDto.getEmail());
        }
        var user = new User();
        user.setName(registerDto.getName());
        user.setCreatedAt(LocalDateTime.now());
        user.setRole(Role.USER);
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        userRepository.save(user);
        return "Registered";
    }


    @Async
    protected void updateLastLoginAsync(Authentication authentication) {
        //TODO duplikacja kodu + zastanowić się czy warto asynchroniecznie
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("No authenticated user found");
        }
        String userMail = authentication.getName();
        User user = userRepository.findByEmail(userMail)
                .orElseThrow(() -> new UserNotFoundException(userMail));
        user.setLastLoggedInAt(LocalDateTime.now());
        userRepository.save(user);
    }

}
