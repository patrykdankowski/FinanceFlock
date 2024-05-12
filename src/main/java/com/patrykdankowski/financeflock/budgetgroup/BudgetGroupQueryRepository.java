package com.patrykdankowski.financeflock.budgetgroup;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface BudgetGroupQueryRepository extends Repository<BudgetGroup, Long> {

    Optional<BudgetGroup> findBudgetGroupById(long id);


}
