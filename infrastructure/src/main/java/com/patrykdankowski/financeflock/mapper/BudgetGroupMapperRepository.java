package com.patrykdankowski.financeflock.mapper;

import com.patrykdankowski.financeflock.budgetgroup.BudgetGroupSqlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetGroupMapperRepository extends JpaRepository<BudgetGroupSqlEntity,Long> {


}
