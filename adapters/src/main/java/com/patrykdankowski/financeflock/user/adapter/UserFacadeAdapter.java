package com.patrykdankowski.financeflock.user.adapter;

import com.patrykdankowski.financeflock.auth.port.AuthenticationServicePort;
import com.patrykdankowski.financeflock.budgetgroup.model.entity.BudgetGroupDomainEntity;
import com.patrykdankowski.financeflock.budgetgroup.port.BudgetGroupCommandServicePort;
import com.patrykdankowski.financeflock.common.Role;
import com.patrykdankowski.financeflock.user.model.entity.UserDomainEntity;
import com.patrykdankowski.financeflock.user.port.UserCommandServicePort;
import com.patrykdankowski.financeflock.user.port.UserFacadePort;
import com.patrykdankowski.financeflock.user.port.UserMembershipDomainPort;
import com.patrykdankowski.financeflock.user.port.UserValidatorPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
 class UserFacadeAdapter implements UserFacadePort {

    private final BudgetGroupCommandServicePort budgetGroupCommandService;
    private final UserMembershipDomainPort userMembershipDomain;
    private final UserCommandServicePort userCommandService;
    private final AuthenticationServicePort authenticationService;
    private final UserValidatorPort userValidator;

    UserFacadeAdapter(final BudgetGroupCommandServicePort budgetGroupCommandService,
                      final UserMembershipDomainPort userMembershipDomain,
                      final UserCommandServicePort userCommandService,
                      final AuthenticationServicePort authenticationService, final UserValidatorPort userValidator) {
        this.budgetGroupCommandService = budgetGroupCommandService;
        this.userMembershipDomain = userMembershipDomain;
        this.userCommandService = userCommandService;
        this.authenticationService = authenticationService;
        this.userValidator = userValidator;
    }


    @Override
    public void leaveBudgetGroup(final Long id) {

        final UserDomainEntity loggedUser = authenticationService.getUserFromContext();
        final BudgetGroupDomainEntity budgetGroup = budgetGroupCommandService.findBudgetGroupById(loggedUser.getBudgetGroupId());

        boolean hasRole = userValidator.hasGivenRole(loggedUser, Role.GROUP_MEMBER);


        userMembershipDomain.leaveBudgetGroup(loggedUser, budgetGroup,hasRole, id);


        userCommandService.saveUser(loggedUser);
        budgetGroupCommandService.saveBudgetGroup(budgetGroup);
        //TODO -> informowanie założyciela przez wysłanie mail'a, że user opuścił grupę
    }

    @Override
    public boolean toggleShareData(){

        final UserDomainEntity loggedUser = authenticationService.getUserFromContext();

        boolean isSharingData = userMembershipDomain.toggleShareData(loggedUser);
        userCommandService.saveUser(loggedUser);

        return isSharingData;
    }


}
