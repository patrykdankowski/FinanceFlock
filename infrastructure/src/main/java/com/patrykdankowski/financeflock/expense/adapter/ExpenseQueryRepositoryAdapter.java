package com.patrykdankowski.financeflock.expense.adapter;

import com.patrykdankowski.financeflock.expense.dto.ExpenseDto;
import com.patrykdankowski.financeflock.expense.entity.ExpenseSqlEntity;
import com.patrykdankowski.financeflock.expense.port.ExpenseQueryRepositoryPort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

 interface ExpenseQueryRepositoryAdapter extends ExpenseQueryRepositoryPort, Repository<ExpenseSqlEntity, Long> {


    @Query("SELECT new com.patrykdankowski.financeflock.expense.dto.ExpenseDto(e.description, e.amount, e.location) " +
            "FROM ExpenseSqlEntity e WHERE e.user.id = :userId")
    List<ExpenseDto> findExpensesForUser(@Param("userId") Long userId);

}
