package com.patrykdankowski.financeflock.user;

import com.patrykdankowski.financeflock.common.UserAndGroupUpdateResult;

public interface UserMembershipDomain {


    UserAndGroupUpdateResult leaveBudgetGroup();

    User toggleShareData();

}
