package com.patrykdankowski.financeflock.repository;

import com.patrykdankowski.financeflock.entity.BudgetGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetGroupRepository extends JpaRepository<BudgetGroup, Long> {
}
