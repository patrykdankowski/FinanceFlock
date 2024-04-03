package com.patrykdankowski.financeflock.service;

import com.patrykdankowski.financeflock.entity.BudgetGroup;
import com.patrykdankowski.financeflock.entity.Role;
import com.patrykdankowski.financeflock.entity.User;
import com.patrykdankowski.financeflock.exception.UserNotFoundException;
import com.patrykdankowski.financeflock.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void leaveBudgetGroup() {
        Authentication authentication = getAuthentication();

        String userEmail = authentication.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail));

        BudgetGroup budgetGroup = user.getBudgetGroup();
        if (budgetGroup == null) {
            throw new IllegalStateException(user.getName() + " does not have a group");
        }
        user.setBudgetGroup(null);
        user.setRole(Role.USER);
        userRepository.save(user);
        //TODO -> informowanie założyciela przez wysłanie mail'a, że user opuścił grupę
    }

    private Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("No authenticated user found");
        }
        return authentication;
    }
}
