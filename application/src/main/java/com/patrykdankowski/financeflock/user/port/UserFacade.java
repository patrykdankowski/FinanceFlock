package com.patrykdankowski.financeflock.user.port;

import jakarta.transaction.Transactional;

public interface UserFacade {
    @Transactional
    void leaveBudgetGroup(final Long id);

    @Transactional
    boolean toggleShareData();
}
