package com.patrykdankowski.financeflock.user;

import com.patrykdankowski.financeflock.common.UserAndGroupUpdateResult;

 interface UserMembershipDomain {


    UserAndGroupUpdateResult leaveBudgetGroup();

    User toggleShareData();

}
