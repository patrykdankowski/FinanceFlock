package com.patrykdankowski.financeflock.user;

import com.patrykdankowski.financeflock.auth.AuthenticationService;
import com.patrykdankowski.financeflock.auth.UserContextService;
import com.patrykdankowski.financeflock.budgetgroup.BudgetGroup;
import com.patrykdankowski.financeflock.cache.UserCacheService;
import com.patrykdankowski.financeflock.constants.Role;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserFacade {

    UserFacade(final UserService userService, final UserContextService userContextService, final UserCacheService userCacheService, final AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Transactional
    void leaveBudgetGroup() {
        final User userFromContext = authenticationService.getUserFromContext();
        BudgetGroup budgetGroup = userFromContext.getBudgetGroup();
        if (budgetGroup == null) {
            throw new IllegalStateException(userFromContext.getName() + " does not have a group");
        }
        userFromContext.setBudgetGroup(null);
        userFromContext.setRole(Role.USER);
        userService.saveUser(userFromContext);
        //TODO -> informowanie założyciela przez wysłanie mail'a, że user opuścił grupę
    }

    boolean toggleShareData() {
        final User userFromContext = authenticationService.getUserFromContext();
        userFromContext.setShareData(!userFromContext.isShareData());
        userService.saveUser(userFromContext);
        return userFromContext.isShareData();
    }


}
