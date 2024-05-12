package com.patrykdankowski.financeflock.user;

import com.patrykdankowski.financeflock.auth.AuthenticationService;
import com.patrykdankowski.financeflock.budgetgroup.BudgetGroup;
import com.patrykdankowski.financeflock.budgetgroup.BudgetGroupCommandService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserFacadeImpl implements UserFacade {

    private final BudgetGroupCommandService budgetGroupCommandService;
    private final UserMembershipDomain userMembershipDomain;
    private final UserCommandService userCommandService;
    private final AuthenticationService authenticationService;

    UserFacadeImpl(final BudgetGroupCommandService budgetGroupCommandService,
                   final UserMembershipDomain userMembershipDomain,
                   final UserCommandService userCommandService, final AuthenticationService authenticationService) {
        this.budgetGroupCommandService = budgetGroupCommandService;
        this.userMembershipDomain = userMembershipDomain;
        this.userCommandService = userCommandService;
        this.authenticationService = authenticationService;
    }


    @Override
    public void leaveBudgetGroup() {

        final User userFromContext = authenticationService.getUserFromContext();
        final BudgetGroup budgetGroup = userMembershipDomain.leaveBudgetGroup(userFromContext);


        userCommandService.saveUser(userFromContext);
        budgetGroupCommandService.saveBudgetGroup(budgetGroup);
        //TODO -> informowanie założyciela przez wysłanie mail'a, że user opuścił grupę
    }

    @Override
    public boolean toggleShareData() {
        final User userFromContext = authenticationService.getUserFromContext();
        boolean isSharingData = userMembershipDomain.toggleShareData(userFromContext);
        userCommandService.saveUser(userFromContext);

        return isSharingData;
    }


}
