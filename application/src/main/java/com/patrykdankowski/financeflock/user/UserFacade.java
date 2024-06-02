package com.patrykdankowski.financeflock.user;

import jakarta.transaction.Transactional;

 interface UserFacade {
    @Transactional
    void leaveBudgetGroup(final Long id);

    @Transactional
    boolean toggleShareData();
}
