package com.patrykdankowski.financeflock.user;

import com.patrykdankowski.financeflock.auth.AuthenticationServicePort;
import com.patrykdankowski.financeflock.budgetgroup.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.BudgetGroupCommandServicePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserFacadeImpl implements UserFacade {

    private final BudgetGroupCommandServicePort budgetGroupCommandService;
    private final UserMembershipDomainPort userMembershipDomain;
    private final UserCommandServicePort userCommandService;
    private final AuthenticationServicePort authenticationService;

    UserFacadeImpl(final BudgetGroupCommandServicePort budgetGroupCommandService,
                   final UserMembershipDomainPort userMembershipDomain,
                   final UserCommandServicePort userCommandService,
                   final AuthenticationServicePort authenticationService) {
        this.budgetGroupCommandService = budgetGroupCommandService;
        this.userMembershipDomain = userMembershipDomain;
        this.userCommandService = userCommandService;
        this.authenticationService = authenticationService;
    }


    @Override
    public void leaveBudgetGroup() {

        final UserDomainEntity userFromContext = authenticationService.getUserFromContext();
        final BudgetGroupDomainEntity budgetGroupDomainEntity = userMembershipDomain.leaveBudgetGroup(userFromContext);


        userCommandService.saveUser(userFromContext);
        budgetGroupCommandService.saveBudgetGroup(budgetGroupDomainEntity);
        //TODO -> informowanie założyciela przez wysłanie mail'a, że user opuścił grupę
    }

    @Override
    public boolean toggleShareData() {
        final UserDomainEntity userFromContext = authenticationService.getUserFromContext();
        boolean isSharingData = userMembershipDomain.toggleShareData(userFromContext);
        userCommandService.saveUser(userFromContext);

        return isSharingData;
    }


}
