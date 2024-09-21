package com.patrykdankowski.financeflock.expense.port;

import com.patrykdankowski.financeflock.expense.dto.ExpenseDto;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExpenseQueryRepositoryPort {

    List<ExpenseDto> findExpensesForUser(@Param("userId") Long userId);

}
