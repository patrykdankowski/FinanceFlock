package com.patrykdankowski.financeflock.service;

import com.patrykdankowski.financeflock.entity.BudgetGroup;
import com.patrykdankowski.financeflock.entity.Role;
import com.patrykdankowski.financeflock.entity.User;
import com.patrykdankowski.financeflock.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserContextService userContextService;
    private final UserCacheService userCacheService;

    @Transactional
    public void leaveBudgetGroup() {
        Authentication authentication = userContextService.getAuthentication();

        String userEmail = authentication.getName();
        User user = userCacheService.getUserFromEmail(userEmail);

        BudgetGroup budgetGroup = user.getBudgetGroup();
        if (budgetGroup == null) {
            throw new IllegalStateException(user.getName() + " does not have a group");
        }
        user.setBudgetGroup(null);
        user.setRole(Role.USER);
        userRepository.save(user);
        //TODO -> informowanie założyciela przez wysłanie mail'a, że user opuścił grupę
    }


}
