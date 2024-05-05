package com.patrykdankowski.financeflock.budgetgroup;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface BudgetGroupCommandRepository extends Repository<BudgetGroup, Long> {

    Optional<BudgetGroup> findById(long id);

    BudgetGroup save(BudgetGroup budgetGroup);

    void delete(BudgetGroup budgetGroup);

}
