package com.patrykdankowski.financeflock.service.impl;

import com.patrykdankowski.financeflock.dto.LoginDto;
import com.patrykdankowski.financeflock.dto.RegisterDto;
import com.patrykdankowski.financeflock.entity.Role;
import com.patrykdankowski.financeflock.entity.User;
import com.patrykdankowski.financeflock.repository.RoleRepository;
import com.patrykdankowski.financeflock.repository.UserRepository;
import com.patrykdankowski.financeflock.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "Logged in";
    }

    @Override
    public String register(RegisterDto registerDto) {
        if(userRepository.existsUserByEmail(registerDto.getEmail())){
            // zrobic customowy wyjatek
            throw new RuntimeException("Email already exists in db;");
        }
        var user = new User();
        Role roleUser = roleRepository.findByName("ROLE_USER").get();
        user.setFirstName(registerDto.getName());
        user.setCreatedAt(LocalDateTime.now());
        user.setRoles(Set.of(roleUser));
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        userRepository.save(user);
        return "Registered";
    }
}
