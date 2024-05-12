package com.patrykdankowski.financeflock.user;

import jakarta.transaction.Transactional;

public interface UserFacade {
    @Transactional
    void leaveBudgetGroup();

    @Transactional
    boolean toggleShareData();
}
