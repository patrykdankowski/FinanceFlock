package com.patrykdankowski.financeflock.user;

import com.patrykdankowski.financeflock.budgetgroup.BudgetGroupService;
import com.patrykdankowski.financeflock.common.UserAndGroupUpdateResult;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserFacade {

    private final BudgetGroupService budgetGroupService;
    private final UserMembershipDomain userMembershipDomain;
    private final UserService userService;

    public UserFacade(final BudgetGroupService budgetGroupService,
                      final UserMembershipDomain userMembershipDomain,
                      final UserService userService) {
        this.budgetGroupService = budgetGroupService;
        this.userMembershipDomain = userMembershipDomain;
        this.userService = userService;
    }


    @Transactional
    void leaveBudgetGroup() {

        final UserAndGroupUpdateResult<User> userAndGroupUpdateResult = userMembershipDomain.leaveBudgetGroup();

        userService.saveUser(userAndGroupUpdateResult.getSource());
        budgetGroupService.saveBudgetGroup(userAndGroupUpdateResult.getBudgetGroup());
        //TODO -> informowanie założyciela przez wysłanie mail'a, że user opuścił grupę
    }

    @Transactional
    boolean toggleShareData() {
        final User user = userMembershipDomain.toggleShareData();
        userService.saveUser(user);
        return user.isShareData();
    }


}
