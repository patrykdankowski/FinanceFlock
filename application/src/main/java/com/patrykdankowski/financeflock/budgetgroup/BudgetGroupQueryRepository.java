package com.patrykdankowski.financeflock.budgetgroup;

import org.springframework.data.repository.Repository;

import java.util.Optional;

 interface BudgetGroupQueryRepository extends Repository<BudgetGroup, Long> {

    Optional<BudgetGroup> findBudgetGroupById(long id);


}
